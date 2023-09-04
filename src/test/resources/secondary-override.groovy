import com.boomi.connector.api.*
import com.boomi.connector.groovy.test.CustomListenConnector

connector(CustomListenConnector) {

    silly = {
        "xxx"
    }

    browser {

        multiplier { 
            100
        }

        getObjectTypes {
                ObjectTypes types = new ObjectTypes();
                for (int i = 1; i <= 5; i++) {
                    types.getTypes().add(new ObjectType().withId(String.valueOf(i * multiplier())));
                }
                return types;
        }

    }

    // operations {

    //     "opop" { 
    //         execute { request, response -> 
    //             request.each { data -> 
    //                 response.addResult(data, OperationStatus.APPLICATION_ERROR, "400", "bad things!", null)
    //             }
    //         }
    //     }

    //     "listen" {
    //         listen {

    //             def x 

    //             start { listener -> 
    //                 x = 1 
    //                 listener.submit(PayloadUtil.toPayload("i did it"))
    //             }

    //             stop {
    //                 println x
    //             }
                
    //         }
    //     }

    // }

}

