## Disclaimer

Since Postman required to create an account, I decided to use the open source tool "Insomnia" for testing, since it
works quite similar.
Unfortunately it was not possible for me to export my tests in a Postman-readable format, therefore the JSON (
task_8_api_tests.json) needs to be tested with Insomnia too.

## Encountered Bugs/Unexpected behavior

- creating an existing customer again causes an error 400 on CustomerManagementController-Endpoint but a 500 on
  CustomerRegistrationController-Endpoint
- creating an existing customer twice causes an IllegalStateException, with telling the client an error 500
    - should be an error 4xx and give a more detailed error message to client
    - maybe the code lacks a proper exception handling as found in the CustomerManagementController-Endpoint
