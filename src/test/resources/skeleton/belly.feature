Feature: Belly


  Scenario: a google map request
    Given I have a "https://www.google.com/maps/dir/" as my url
    When I invoke it with "San Francisco, CA 94102" and "San Francisco, CA 94104" as my parameters
    Then the distance and duration are validated against the API