# SonarLint Report for Task 5

## Own Tests

Initial:

| Class                           | Bugs | Vulnerabilities | Code Smells          |
|---------------------------------|------|-----------------|----------------------| 
| CustomerManagementServiceTest   | 0    | 0               | 4 Major<br/> 12 Info |
| CustomerRegistrationServiceTest | 0    | 0               | 3 Info               |
| CustomerRepositoryTest          | 0    | 0               | 10 Info              |
| MiscTest                        | 0    | 0               | 1 Blocker            | `
| PhoneNumberValidatorTest        | 0    | 0               | 9 Info               | 

After fixing:

| Class                           | Bugs | Vulnerabilities | Code Smells        |
|---------------------------------|------|-----------------|--------------------|
| CustomerManagementServiceTest   | 0    | 0               | 0 Major<br/>0 Info |
| CustomerRegistrationServiceTest | 0    | 0               | 0 Info             |
| CustomerRepositoryTest          | 0    | 0               | 0 Info             |
| MiscTest                        | 0    | 0               | 0 Blocker          | `
| PhoneNumberValidatorTest        | 0    | 0               | 0 Info             | 

## FOSS Project: Tic Tac Toe Game

from: https://github.com/LazoCoder/Tic-Tac-Toe
Initial:

| Class             | Bugs       | Vulnerabilities | Code Smells                        |
|-------------------|------------|-----------------|------------------------------------| 
| Algorithms        | 0          | 0               | 1 Minor                            |
| AlphaBetaAdvanced | 0          | 0               | 1 Minor                            |
| AlphaBetaPruning  | 0          | 0               | 1 Minor                            |
| Board             | 0          | 0               | 1 Critical<br/>1 Major<br/>2 Minor |
| Console           | 0          | 0               | 15 Major<br/>1 Minor               |
| MiniMax           | 0          | 0               | 1 Minor                            |
| Random            | 1 Critical | 0               | 1 Minor                            |
| Window            | 0          | 0               | 4 Critical<br/>4 Major<br/>2 Minor |

After fixing:

| Class             | Bugs       | Vulnerabilities | Code Smells                        |
|-------------------|------------|-----------------|------------------------------------| 
| Algorithms        | 0          | 0               | 1 Minor                            |
| AlphaBetaAdvanced | 0          | 0               | 1 Minor                            |
| AlphaBetaPruning  | 0          | 0               | 1 Minor                            |
| Board             | 0          | 0               | 1 Critical<br/>1 Major<br/>2 Minor |
| Console           | 0          | 0               | 15 Major<br/>1 Minor               |
| MiniMax           | 0          | 0               | 1 Minor                            |
| Random            | 1 Critical | 0               | 1 Minor                            |
| Window            | 0          | 0               | 4 Critical<br/>4 Major<br/>2 Minor |