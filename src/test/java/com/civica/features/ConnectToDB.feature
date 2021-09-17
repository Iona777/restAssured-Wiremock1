Feature:  I connect to a MySQL DB

  Scenario Outline: I connect to DB
    #Better to read the SQL in from a file (see CC repo?), but this will do for now
    Given I connect to the DB with "<URL>", "<Username>" and "<Password>"
    When I execute the SQL query in file "fileName"
    #When I execute the SQL query "<QueryString>"
    Then I print out the names
    Examples:
      | URL                                            | Username   | Password   | QueryString                                    |
      | jdbc:mysql://localhost/test?serverTimezone=UTC | myUserName | MyPassword | select * from users where first_name =  'John' |