import com.boomi.connector.api.*
import com.boomi.connector.groovy.test.CustomListenConnector

connector(CustomListenConnector) {

    browser {

        getObjectTypes {
            new ObjectTypes().withTypes(new ObjectType().withId("a"), new ObjectType().withId("b"), new ObjectType().withId("c"))
        }

        getObjectDefinitions { String objectTypeId, Set roles -> 
            new ObjectDefinitions().withDefinitions(new ObjectDefinition().withInputType(ContentType.NONE).withOutputType(ContentType.NONE))
        }

    }

    operations {

        "opop" { 
            execute { request, response -> 
                request.each { data -> 
                    response.addResult(data, OperationStatus.APPLICATION_ERROR, "400", "bad things!", null)
                }
            }
        }

        "listen" {
            listen {

                def x 

                start { listener -> 
                    x = 1 
                    listener.submit(PayloadUtil.toPayload("i did it"))
                }

                stop {
                    println x
                }
                
            }
        }

    }

}

