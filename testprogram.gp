(iterate                       
    (subtract (index) (length))  
    (swap (index) (length))     
    (getbigger 
        (decrement 
            (iterate            
                (index)        
                (length)        
                (swap          
                    (index) 
                    (getbigger 
                        (decrement (index)) 
                        (swap (index) (index))
                    )
                )
            )
        ) 
        (length)
    )
)
