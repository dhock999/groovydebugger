import com.boomi.connector.api.*

connector {

    browser {

        getObjectTypes {
            new ObjectTypes().withTypes(new ObjectType().withId("x"), new ObjectType().withId("y"), new ObjectType().withId("z"))
        }

        getObjectDefinitions { objectTypeId, roles -> 
            new ObjectDefinitions().withDefinitions(new ObjectDefinition().withInputType(ContentType.BINARY).withOutputType(ContentType.BINARY))
        }

    }

    operations {

        "good" { 
            execute { request, response -> 
                request.each { data -> 
                    response.addResult(data, OperationStatus.SUCCESS, "200", "great success!", null)
                }
            }
        } 

        "bad" { 
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
                    listener.submit(PayloadUtil.toPayload("don't use strings as payloads"))
                }

                stop {
                    println x
                }
                
            }
        }

    }

}

