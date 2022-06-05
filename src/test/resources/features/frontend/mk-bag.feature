@test @MK

  Feature: Verifying price of MK Bags

    Background: Check for internet connectivity
      Given User has internet connectivity

    Scenario: Verifying price of Tote MK Bag
      Given User navigates to Google
      And The page is loaded
      And User searches for "Michael Kors" on google
      And User clicks on on partial link text "Michael Kors USA:"
      And User closes all popups
      And User taps on SearchBar
      When User searches for "tote bags"
      And User opens "Voyager Medium Logo Tote Bag" from search results
      Then The price of bag is "Rs80,700"