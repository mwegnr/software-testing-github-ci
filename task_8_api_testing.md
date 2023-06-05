## Disclaimer

Since Postman required to create an account, I decided to use the open source tool "Insomnia" for testing, since it
works quite similar.
Unfortunately it was not possible for me to export my tests in a Postman-readable format, therefore the JSON (
task_8_api_tests.json) needs to be tested with Insomnia too.

## API-Calls tested

CustomerRegistration-Endpoint:

| Endpoint                      | Method | Body                                                                                            | Notes              | Expected Response | Actual Response | 
|-------------------------------|--------|-------------------------------------------------------------------------------------------------|--------------------|-------------------|-----------------| 
| /api/v1/customer-registration | GET    | -                                                                                               | Method not allowed | 405               | 405             | 
| /api/v1/customer-registration | POST   | {"userName": "ame7AMi9an", </br> "name": "Ralf Strauss", </br> "phoneNumber": "+499829815965" } | -                  | 200               | 200             | 
| /api/v1/customer-registration | POST   | { "userName": "ame7A9an", </br> "name": "Ralf Amsel", </br> "phoneNumber": "+4998298165"}       | Existing number    | 400               | 400             |
| /api/v1/customer-registration | POST   | { "userName": "ame7A9an", </br> "name": "Ralf Amsel", </br> "phoneNumber": "+4998298165"}       | Existing user      | 400               | *500*           |
| /api/v1/customer-registration | DELETE | -                                                                                               | Method not allowed | 405               | 405             | 

CustomerManagement-Endpoint:

| Endpoint                | Method | Body                                                                                            | Notes           | Expected Response | Actual Response | 
|-------------------------|--------|-------------------------------------------------------------------------------------------------|-----------------|-------------------|-----------------| 
| /api/v1/customers/20000 | GET    | -                                                                                               | -               | 200               | 200             | 
| /api/v1/customers/50000 | GET    | -                                                                                               | Non-existent ID | 404               | 404             | 
| /api/v1/customers/list  | GET    | -                                                                                               | -               | 200               | 200             | 
| /api/v1/customers/      | POST   | {"userName": "ame7AMi9an", </br> "name": "Ralf Strauss", </br> "phoneNumber": "+499829815965" } | -               | 200               | 200             | 
| /api/v1/customer/       | POST   | { "userName": "ame7A9an", </br> "name": "Ralf Amsel", </br> "phoneNumber": "+4998298165"}       | Existing number | 400               | 400             |
| /api/v1/customer/       | POST   | { "userName": "ame7A9an", </br> "name": "Ralf Amsel", </br> "phoneNumber": "+4998298165"}       | Existing user   | 400               | 400             |
| /api/v1/customer/20000  | DELETE | -                                                                                               | -               | 200               | 200             | 
| /api/v1/customer/50000  | DELETE | -                                                                                               | Non-existent ID | 404               | 404             | 

## Encountered Bugs/Unexpected behavior

- creating an existing customer again causes an error 400 on CustomerManagementController-Endpoint but a 500 on
  CustomerRegistrationController-Endpoint
- creating an existing customer twice causes an IllegalStateException, with telling the client an error 500
    - should be an error 4xx and give a more detailed error message to client
    - maybe the code lacks a proper exception handling as found in the CustomerManagementController-Endpoint

