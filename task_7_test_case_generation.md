|                   | Own Tests    | Diffblue | SquareTest | 
|-------------------|--------------|----------|------------| 
| Generation Time   | -            | 190s     | 60s        |
| Line Coverage     | 92/170 = 54% | 72/170   | 39/170     | 
| Mutation Coverage | 57/94 = 61%  | 40/94    | 21/94      |
| Test Strength     | 57/67 = 85%  | 40/67    | 21/27      |

## Line Coverage

|                              | Own Tests | Diffblue | SquareTest | 
|------------------------------|-----------|----------|------------| 
| CustomerManagementServiceImp | 23/23     | 21/23    | 12/23      |
| CustomerRegistrationService  | 11/11     | 11/11    | 6/11       |
| DebugFactorial               | 6/6       | 4/6      | 0/6        |
| Misc                         | 22/23     | 19/23    | 3/23       |
| CustomerValidator            | 5/5       | 3/5      | 3/5        |
| PhoneNumberValidator         | 5/5       | 4/5      | 4/5        |

## Mutation Coverage

|                              | Own Tests | Diffblue | SquareTest | 
|------------------------------|-----------|----------|------------| 
| CustomerManagementServiceImp | 12/12     | 10/12    | 6/12       |
| CustomerRegistrationService  | 3/3       | 3/3      | 2/3        |
| DebugFactorial               | 4/4       | 4/4      | 0/4        |
| Misc                         | 30/33     | 19/33    | 3/33       |
| CustomerValidator            | 1/1       | 1/1      | 1/1        |
| PhoneNumberValidator         | 3/3       | 2/3      | 2/3        |

## Test Strength

|                              | Own Tests | Diffblue | SquareTest | 
|------------------------------|-----------|----------|------------| 
| CustomerManagementServiceImp | 12/12     | 10/10    | 6/6        |
| CustomerRegistrationService  | 3/3       | 3/3      | 2/2        |
| DebugFactorial               | 4/4       | 4/4      | 0/0        |
| Misc                         | 30/32     | 19/22    | 3/6        |
| CustomerValidator            | 1/1       | 1/1      | 1/1        |
| PhoneNumberValidator         | 3/3       | 2/3      | 2/3        |

## Meaningful Tests

Squaretest generated many failing tests that were useless (e.g. divide by 0 with expected result 0, expected
factorial of 0 to be 0, etc.). It was necessary to disable a lot of tests to get a coverage report. Also this resulted
in a weak coverage. (39/170 vs 92/170 in handwritten tests)

Diffblue checked the generated tests and disabled the failing one by itself. It usually generated more meaningful tests
and therefore fewer are disabled. This resulted in a quite good coverage. (72/170 vs 92/170 in handwritten tests)