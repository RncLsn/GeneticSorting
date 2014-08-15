package genetic_sorting.batch_gp;

import genetic_sorting.util.SimpleLogger;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Alessandro Ronca
 */
public class ExperimentsBatchGP {

    /*
     * Default values:
     *
     * initialPopulation=1000
     * maxTreeDepth=6
     * testCases=30
     * maxListLength=50
     * maxListValue=100
     * mutationPct=0.1
     * crossoverPct=0.9
     * crossoverExpansion=1.8
     * generations=50
     *
     * runs=20
     *
     * generalTestCases=1000
     * generalMaxListLength=100
     * generalMaxListValue=200
     *
     * */

    //    private static final int  RUNS               = 100;
    private static final int    RUNS               = 100;
    private static final int    PARALLELISM_DEGREE = 4;
    private static final long   WAIT_TIME          = 10;
    private static final String OUT_RES_PATH       = "BatchResults/BatchGPOutputs";
    private static final String OUT_PATH           = "BatchResults";


    public static void main (String[] args) {

//        List<Double> mutationPctValues = Arrays.asList(0.0, 0.1, 0.2, 0.3, 0.4, 0.5);
//        List<Double> crossoverPctValues = Arrays.asList(0.5, 0.6, 0.7, 0.8, 0.9, 1.0);
//        List<Integer> generationsValues = Arrays.asList(25, 50, 75);
//        List<Integer> testCasesValues     = Arrays.asList(1, 5, 10, 20, 40, 80);
//        List<Integer> maxListLengthValues = Arrays.asList(2, 4, 8, 16, 32, 64);

        List<Double> mutationPctValues = Arrays.asList(0.1, 0.2, 0.3);
        List<Double> crossoverPctValues = Arrays.asList(0.7, 0.8, 0.9);
        List<Integer> generationsValues = Arrays.asList(50);
        List<Integer> testCasesValues = Arrays.asList(5, 15, 30, 50);
        List<Integer> maxListLengthValues = Arrays.asList(7, 23, 49);


        int numOfTests = mutationPctValues.size() * crossoverPctValues.size() *
                         generationsValues.size() * testCasesValues.size() *
                         maxListLengthValues.size();

        OutputStream logOutStream = null;
        try {
            if (Files.notExists(Paths.get(OUT_PATH))) {
                Files.createDirectory(Paths.get(OUT_PATH));
            }
            if (Files.notExists(Paths.get(OUT_RES_PATH))) {
                Files.createDirectory(Paths.get(OUT_RES_PATH));
            }

            logOutStream =
                    Files.newOutputStream(
                            Paths.get(OUT_PATH + File.separator + "Experiments-statistics.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        SimpleLogger logger = new SimpleLogger(Arrays.asList(System.out, logOutStream),
                                               ExperimentsBatchGP.class);

        logger.log("Starting...");

        logger.log("Parameters: mutationPctValues: " + mutationPctValues);
        logger.log("Parameters: crossoverPctValues: " + crossoverPctValues);
        logger.log("Parameters: generationValues: " + generationsValues);
        logger.log("Parameters: testCasesValues: " + testCasesValues);
        logger.log("Parameters: maxListLengthValues: " + maxListLengthValues);

        logger.log("Trying all combinations gives a total number of tests:" + numOfTests);
        logger.log("Tests run in parallel:" + PARALLELISM_DEGREE);

        Map<BatchGP, Thread> runningBatchesMap =
                Collections.synchronizedMap(new HashMap<BatchGP, Thread>());

        int batchNumber = 0;
        for (double mutationPct : mutationPctValues) {
            for (double crossoverPct : crossoverPctValues) {
                for (int generations : generationsValues) {
                    for (int testCases : testCasesValues) {
                        for (int maxListLength : maxListLengthValues) {

                            // wait for completion of some batch
                            while (runningBatchesMap.size() >= PARALLELISM_DEGREE) {
                                try {
                                    Thread.sleep(WAIT_TIME);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            BatchProperties batchProperties = new BatchProperties();
                            batchProperties.setDoubleProperty("mutationPct", mutationPct);
                            batchProperties.setDoubleProperty("crossoverPct", crossoverPct);
                            batchProperties.setIntProperty("generations", generations);
                            batchProperties.setIntProperty("testCases", testCases);
                            batchProperties.setIntProperty("maxListLength", maxListLength);
                            batchProperties.setIntProperty("maxListValue", maxListLength * 2);
                            batchProperties.setIntProperty("runs", RUNS);

                            OutputStream batchOutputStream = null;
                            try {
                                batchOutputStream = Files
                                        .newOutputStream(Paths.get(OUT_RES_PATH + File.separator +
                                                                   "batch_n" + batchNumber));
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.exit(1);
                            }
                            BatchGP batchGPRunnable = new BatchGP(batchProperties,
                                                                  Arrays.asList(batchOutputStream));
                            Thread batchGPThread = new Thread(batchGPRunnable);
                            batchGPThread.start();
                            runningBatchesMap.put(batchGPRunnable, batchGPThread);
                            new Thread(new ThreadHandler(batchGPRunnable, runningBatchesMap,
                                              /*completedBatches,*/ logger)).start();
                            batchNumber++;
                        }
                    }
                }
            }
        }
        try {
            logOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static class ThreadHandler implements Runnable {

        private final BatchGP              batchGP;
        private final Map<BatchGP, Thread> threadMap;
        private final SimpleLogger         logger;

        private ThreadHandler (BatchGP batchGP, Map<BatchGP, Thread> threadMap,
                               SimpleLogger logger) {
            this.threadMap = threadMap;
            this.batchGP = batchGP;
            this.logger = logger;
        }

        @Override
        public void run () {
            try {
                threadMap.get(batchGP).join();
                threadMap.remove(batchGP);

                outputResult();

            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        private void outputResult () {
            logger.log("BatchGP completed with parameters: " + batchGP.getProperties() +
                       "\nResults: " +
                       "AvgGeneralSortings=" + batchGP.getAvgGeneralSortings() +
                       ", AvgCorrectSortings=" + batchGP.getAvgCorrectSortings() +
                       ", GeneralityRatio=" + batchGP.getGeneralityRatio() +
                       ", AvgGenerationsOnSuccessfulRuns=" +
                       batchGP.getAvgGenerationsOnSuccessfulRuns() +
                       ", AvgGenerationsOnAllRuns=" + batchGP.getAvgGenerationsOnAllRuns());
        }
    }
}
