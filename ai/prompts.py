BASE_PROMPT = """
As a clever and cool QA engineer performing e2e testing you must make sure that all steps in the given test scenario are executed successfully.
**Important**: 
1) If the given test scenario passed successfully, you must output "SUCCESS". The test scenario is considered to be successful only if all the given steps in it were executed successfully.
Successfully means that the result after executing a particular test step is matching with the **Verification** part.
2)A step is considered failed if any explicit verification check you perform 
   (search_page, find_elements, evaluate reading text/values) returns a negative 
   or empty result for that step's required Verification text. In that case, 
   you MUST report "FAILURE" for that step with a specific reason. Do not proceed, and do not report 
   SUCCESS based on the fact that a click or input action was technically executed.
   Never declare "SUCCESS" for a step whose Verification you have not positively confirmed.
3) Do only what's explicitly stated and use the explicitly provided test data, do not improvise!
4) If a click on a button does not produce the expected change after 1 retry with the standard click action, 
   try triggering it via the evaluate/JavaScript action before concluding the failure.
5) After clicking any button that is expected to change the page state (e.g. "Add to 
   cart" → "Remove", cart badge count, form submission, form field updates), wait 1-2 seconds before reading the new state or deciding whether the action succeeded. 
   Do not judge success/failure immediately after the click.
   
6) If a you get an error, do NOT retry the same approach more than 2 times. Report "FAILURE" with this specific reason.
   
*** Test Scenario __SCENARIO_NAME__ ***
**Steps:**
__STEPS__
"""


class Scenario:
    def __init__(self, scenario_name: str, steps: str):
        self.scenario_name = scenario_name
        self.steps = (
            BASE_PROMPT
            .replace("__SCENARIO_NAME__", scenario_name)
            .replace("__STEPS__", steps)
        )

TEST_CASES = {
    "Checkout Flow": """
        1. Go to https://www.saucedemo.com/.
            Verification: 'Swag Labs' title is present with the login form.
        2. Fill in the login from: Username="standard_user", Password="secret_sauce".
            Verification: Username and Password fields are filled with values "standard_user" and "secret_sauce" respectively.         
        3. Click "Login" button. 
            Verification: You are redirected to the inventory page is displayed, the products are displayed, 'Swag Labs' title is present.
        4. Add the product with the name "Sauce Labs Backpack" to the cart by clicking its "Add to cart" button.
            Verification: "Add to cart" button in the product's with the name "Sauce Labs Backpack" frame was changed to "Remove". Cart icon shows "1".
        5. Add the product with the name "Sauce Labs Bike Light" to the cart by clicking its "Add to cart" button.
            Verification: "Add to cart" button in the product's with the name "Sauce Labs Bike Light" frame was changed to "Remove". Cart icon shows "2".
        6. Open the cart page: click on the "cart" icon.
            Verification: 2 products "Sauce Labs Backpack" and "Sauce Labs Bike Light" are listed on the cart page.
            Qnt of each is equal to 1. The price, name, description of each is matching with those on the inventory page. "Remove" button is present in each product frame.
            "Checkout" and "Continue Shopping" buttons are present.
        7. Click "Checkout" button.
            Verification: the form with "First Name", "Last Name", "Zip/Postal Code" is displayed. 
            "Continue" and "Cancel" buttons are present.
        8. Fill in the form: "First Name"="Margosha", "Last Name"="Filipovych", "Zip/Postal Code"="8878".
            Verification: "First Name", "Last Name", "Zip/Postal Code" are filled in with values "Margosha", "Filipovych" and "8878" respectively.
        9. Click "Continue" button.
           Verification: 2 products "Sauce Labs Backpack" and "Sauce Labs Bike Light" are listed.
            Qnt of each is equal to 1. The price, name, description of each is matching with those on the cart page.
           "Price Total": "Item total" value is equal to the sum of these two products' prices and "Tax" value is present. 
           "Total" value is equal to the sum of "Item total" and "Tax" values.
           "Payment Information" and "Shipping Information" are present.
           "Cancel" and "Finish" buttons are present.
        10. Click "Finish" button.
            Verification: the confirmation header "Thank you for your order" is shown.
            "Back Home" and "Generate PDF order" buttons are present.
        11. Click "Generate PDF order" button.
            Verification: the file with the pdf extension was downloaded.
        12. Click "Back Home" button.
            Verification: You are redirected back to the main inventory page, where the products are displayed. 
            The icon cart does not have any numeric value nearby, meaning no products are in the cart.
    """,

    "Login As Locked Out User": """
         1. Go to https://www.saucedemo.com/.
            Verification: 'Swag Labs' title is present with the login form.
         2. Fill in the login from: Username="locked_out_user", Password="secret_sauce".
            Verification: Username and Password fields are filled with values "locked_out_user" and "secret_sauce" respectively.         
         3. Click "Login" button.
            Verification: an error message appears containing "Sorry, this user has been locked out".
    """,

    "Login Empty Password": """
    1. Go to https://www.saucedemo.com/.
        Verification: 'Swag Labs' title is present with the login form.
    2. Fill in the login form: leave the Password field empty (clear if needed), Username="visual_user".
       Verification: Password field is empty. Username field is filled with value "visual_user".
    3. Click "Login" button.
        Verification: an error message appears containing "Password is required".
    """,

    "Login Empty Username": """
        1. Go to https://www.saucedemo.com/.
            Verification: 'Swag Labs' title is present with the login form.
        2. Fill in the login form: leave the Username field empty (clear if needed), Password="secret_sauce".
           Verification: Username field is empty. Password field is filled with value "secret_sauce".
        3. Click "Login" button.
            Verification: an error message appears containing "Username is required".
    """,

    "Login Wrong Credentials": """
        1. Go to https://www.saucedemo.com/.
            Verification: 'Swag Labs' title is present with the login form.
        2. Fill in the login form: Username="Margosha", Password="wow".
           Verification: Username and Password fields are filled with values "Margosha" and "wow" respectively.
        3. Click "Login" button.
           Verification: an error message appears containing "Username and password do not match any user in this service".
    """,

    "Remove Product From Inventory Page": """
    1. Go to https://www.saucedemo.com/.
        Verification: 'Swag Labs' title is present with the login form.
    2. Fill in the login form: Username="standard_user", Password="secret_sauce".
        Verification: Username and Password fields are filled with values "standard_user" and "secret_sauce" respectively.
    3. Click "Login" button.
        Verification: You are redirected to the inventory page, the products are displayed, 'Swag Labs' title is present.
    4. Add the product with the name "Sauce Labs Backpack" to the cart by clicking its "Add to cart" button.
        Verification: "Add to cart" button in the product's with the name "Sauce Labs Backpack" frame was changed to "Remove". Cart icon shows "1".
    5. Remove the "Sauce Labs Backpack" product from the cart: click its "Remove" button on the main inventory page.
        Verification: the "Remove" button changed back to "Add to cart". The cart icon shows no number, meaning 0 items.
    """,

    "Remove Product From Cart Page": """
        1. Go to https://www.saucedemo.com/.
            Verification: 'Swag Labs' title is present with the login form.
        2. Fill in the login form: Username="standard_user", Password="secret_sauce".
            Verification: Username and Password fields are filled with values "standard_user" and "secret_sauce" respectively.
        3. Click "Login" button.
            Verification: You are redirected to the inventory page, the products are displayed, 'Swag Labs' title is present.
        4. Add the product with the name "Sauce Labs Bike Light" to the cart by clicking its "Add to cart" button.
            Verification: "Add to cart" button in the product's with the name "Sauce Labs Bike Light" frame was changed to "Remove". Cart icon shows "1".
        5. Open the cart page: click on the "cart" icon.
            Verification: 1 product with the name "Sauce Labs Bike Light" is listed. Qnt is equal to 1. "Remove" button is present in this product frame.
        6. Remove the product "Sauce Labs Bike Light": click "Remove" button in its frame.
            Verification: no items are listed on the cart page. The cart icon shows no number, meaning 0 items.
    """,

    "Sorting": """
        1. Go to https://www.saucedemo.com/.
            Verification: 'Swag Labs' title is present with the login form.
        2. Fill in the login from: Username="standard_user", Password="secret_sauce".
            Verification: Username and Password fields are filled with values "standard_user" and "secret_sauce" respectively.         
        3. Click "Login" button. 
            Verification: You are redirected to the inventory page is displayed, the products are displayed, 'Swag Labs' title is present.
        4. Click the sort dropdown and select "Price (low to high)".
            Verification: the dropdown text shows "Price (low to high)", and all products are appearing in order from the lowest to highest price.
        5. Click the sort dropdown and select "Price (high to low)".
            Verification: the dropdown text shows "Price (high to low)", and all products are appearing in order from the highest to lowest price.
        6. Click the sort dropdown and select "Name (A to Z)".
            Verification: the dropdown text shows "Name (A to Z)", and all products are appearing in order by their names alphabetically from a to z.
        7.  Click the sort dropdown and select "Name (Z to A)".
            Verification: the dropdown text shows "Name (Z to A)", and all products are appearing in order by their names alphabetically from z to a.
        """,


    "Checkout Missing First Name": """
    1. Go to https://www.saucedemo.com/.
        Verification: 'Swag Labs' title is present with the login form.
    2. Fill in the login form: Username="standard_user", Password="secret_sauce".
        Verification: Username and Password fields are filled with values "standard_user" and "secret_sauce" respectively.
    3. Click "Login" button.
        Verification: You are redirected to the inventory page, the products are displayed, 'Swag Labs' title is present.
    4. Add the product with the name "Sauce Labs Backpack" to the cart by clicking its "Add to cart" button.
       Verification: "Add to cart" button in the product's with the name "Sauce Labs Backpack" frame was changed to "Remove". Cart icon shows "1".
    5. Click "Checkout" button.
        Verification: the form with "First Name", "Last Name", "Zip/Postal Code" is displayed.
        "Continue" and "Cancel" buttons are present.
    6. Fill in the form: leave the "First Name" field empty (clear if needed), "Last Name"="Filipovych", "Zip/Postal Code"="8878".
       Verification: "First Name" is empty. "Last Name" and "Zip/Postal Code" are filled in with values "Filipovych" and "8878" respectively.
    7. Click "Continue" button.
       Verification: an error message appears containing "First Name is required".
    """,

    "Checkout Missing Postal Code": """
        1. Go to https://www.saucedemo.com/.
            Verification: 'Swag Labs' title is present with the login form.
        2. Fill in the login form: Username="standard_user", Password="secret_sauce".
            Verification: Username and Password fields are filled with values "standard_user" and "secret_sauce" respectively.
        3. Click "Login" button.
            Verification: You are redirected to the inventory page, the products are displayed, 'Swag Labs' title is present.
        4. Add the product with the name "Sauce Labs Backpack" to the cart by clicking its "Add to cart" button.
           Verification: "Add to cart" button in the product's with the name "Sauce Labs Backpack" frame was changed to "Remove". Cart icon shows "1".
        5. Click "Checkout" button.
            Verification: the form with "First Name", "Last Name", "Zip/Postal Code" is displayed.
            "Continue" and "Cancel" buttons are present.
        6. Fill in the form: leave the "Zip/Postal Code" field empty (clear if needed), "Last Name"="Filipovych", "First Name"="Margosha".
           Verification: "Zip/Postal Code" is empty. "Last Name" and "First Name" are filled in with values "Filipovych" and "Margosha" respectively.
        7. Click "Continue" button.
           Verification: an error message appears containing "Postal Code is required".
    """,

    "Checkout Missing Last Name": """
        1. Go to https://www.saucedemo.com/.
            Verification: 'Swag Labs' title is present with the login form.
        2. Fill in the login form: Username="standard_user", Password="secret_sauce".
            Verification: Username and Password fields are filled with values "standard_user" and "secret_sauce" respectively.
        3. Click "Login" button.
            Verification: You are redirected to the inventory page, the products are displayed, 'Swag Labs' title is present.
        4. Add the product with the name "Sauce Labs Backpack" to the cart by clicking its "Add to cart" button.
           Verification: "Add to cart" button in the product's with the name "Sauce Labs Backpack" frame was changed to "Remove". Cart icon shows "1".
        5. Click "Checkout" button.
            Verification: the form with "First Name", "Last Name", "Zip/Postal Code" is displayed.
            "Continue" and "Cancel" buttons are present.
        6. Fill in the form: leave the "Last Name" field empty (clear if needed), "First Name"="Margosha", "Zip/Postal Code"="8878".
           Verification: "Last Name" is empty. "First Name" and "Zip/Postal Code" are filled in with values "Margosha" and "8878" respectively.
        7. Click "Continue" button.
           Verification: an error message appears containing "Last Name is required".
    """,

    "Checkout All Fields Empty": """
        1. Go to https://www.saucedemo.com/.
            Verification: 'Swag Labs' title is present with the login form.
        2. Fill in the login form: Username="standard_user", Password="secret_sauce".
            Verification: Username and Password fields are filled with values "standard_user" and "secret_sauce" respectively.
        3. Click "Login" button.
            Verification: You are redirected to the inventory page, the products are displayed, 'Swag Labs' title is present.
        4. Add the product with the name "Sauce Labs Backpack" to the cart by clicking its "Add to cart" button.
           Verification: "Add to cart" button in the product's with the name "Sauce Labs Backpack" frame was changed to "Remove". Cart icon shows "1".
        5. Click "Checkout" button.
            Verification: the form with "First Name", "Last Name", "Zip/Postal Code" is displayed.
            "Continue" and "Cancel" buttons are present.
        6. Leave all the fields empty (clear if needed).
            Verification: "First Name", "Last Name", "Zip/Postal Code" are empty.
        7. Click "Continue" button.
           Verification: an error message appears containing "First Name is required".
    """,

    "Direct Url Access To the Inventory Page Without Login Is Not Acceptable": """
        1. Go directly to https://www.saucedemo.com/inventory.html without logging in.
            Verification: an error message appears containing "You can only access '/inventory.html' when you are logged in.".
    """
}

scenarios = [
    Scenario(scenario_name=name, steps=steps)
    for name, steps in TEST_CASES.items()
]