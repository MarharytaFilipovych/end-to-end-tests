# Test Scenarios

**Test ID:** TC-01

**Title**: Complete Purchase Flow (End-to-End Checkout)
**Objective:** The objective of this test case is to verify that a user can successfully complete a full purchase flow → from login to the order confirmation and its receipt PDF download.

**Test Steps:**

| # | Step | Expected Result |
| --- | --- | --- |
| 1 | Navigate to `https://www.saucedemo.com/` | Login page is displayed. "Swag Labs" title and login form are visible. |
| 2 | Enter username `standard_user` and password `secret_sauce` | Both fields are populated with the entered values. |
| 3 | Click `Login` button | User is redirected to the inventory page. The product list and "Swag Labs" header are displayed. |
| 4 | Click `Add to cart` for `Sauce Labs Backpack` | Button label changes to `Remove`. The cart icon shows "1". |
| 5 | Click `Add to cart` for `Sauce Labs Bike Light` | Button label changes to `Remove`" The cart icon shows "2". |
| 6 | Click the cart icon | The cart page opens, showing both products. The proper name, price, description for each, quantity 1 each. `Checkout` and `Continue Shopping` buttons are visible. |
| 7 | Click `Checkout` | The checkout information form is displayed with `First Name`, `Last Name`, `Zip/Postal Code` fields, `Continue` and  `Cancel` buttons. |
| 8 | Fill in `First Name` = `Margosha`, `Last Name` = `Filipovych`, `Zip/Postal Code` = `678678` | All three fields contain the entered values. |
| 9 | Click `Continue` | Order overview page is shown with both products, correct item total, tax, and total price (item total + tax). The payment and shipping info sections are visible. `Finish` and `Back Home` buttons are visible. |
| 10 | Click `Finish` | Confirmation page is displayed with the header "Thank you for your order". |
| 11 | Click `Back Home` | User is redirected to the inventory page. The cart icon shows no item count (cart is empty). |

**Test ID:** TC-02

**Title**: Cart Management (Add and Remove Products)
**Objective:** The objective of this test case is to verify that a user can add and remove products from the cart, and the cart state remains consistent.

**Test Steps:**

| # | Step | Expected Result |
| --- | --- | --- |
| 1 | Navigate to `https://www.saucedemo.com/` | Login page is displayed. "Swag Labs" title and login form are visible. |
| 2 | Enter username `standard_user` and password `secret_sauce`  | Both fields are populated with the entered values. |
| 3 | Click `Login` button | User is redirected to the inventory page. The product list and "Swag Labs" header are displayed. |
| 4 | Click `Add to cart` for `Sauce Labs Backpack` | Button changes to `Remove`. The cart icon shows "1". |
| 5 | Click `Remove` for `Sauce Labs Backpack` (on the inventory page) | Button reverts to `Add to cart`. The cart icon shows no count (0 items). |
| 6 | Click `Add to cart` for `Sauce Labs Bike Light` | Button changes to `Remove`. The cart icon shows "1”. |
| 7 | Click the cart icon | Cart page opens, showing "Sauce Labs Bike Light" with quantity 1, matching `name`, `price`, `description` from the inventory page. The `Remove` button is present. |
| 8 | Click `Remove` for `Sauce Labs Bike Light` (on the cart page) | Product is removed from the cart list. The cart icon shows no count (0 items). |

**Test ID:** TC-03

**Title**: User Authentication (Successful and Failed Logins)
**Objective:** The objective of this test case is to verify that a user can log in with valid credentials and receive appropriate, specific error messages for invalid login attempts.

**Test Steps:**

| # | Step | Expected Result |
| --- | --- | --- |
| 1 | Navigate to `https://www.saucedemo.com/` | "Swag Labs" title and login form are visible. |
| 2 | Fill in the login form: Username = `standard_user` , Password = `secret_sauce` . Click `Login`. | User is redirected to the inventory page and the products are displayed. |
| 3 | Fill in the login form: Username = `locked_out_user`, Password = `secret_sauce` .  Click `Login`. | Error message "Epic sadface: Sorry, this user has been locked out." is shown |
| 4 | Attempt login with an empty username and password `secret_sauce` . | Error message "Epic sadface: Username is required" is shown |
| 5 | Attempt login with username `standard_user` and an empty password.  | Error message "Epic sadface: Password is required" is shown |
| 6 | Attempt login with username "`standard_user`" and password "`wrong_password`" | Error message "Epic sadface: Username and password do not match any user in this service" is shown. |