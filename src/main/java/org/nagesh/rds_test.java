package org.nagesh;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;


import javax.swing.*;
import java.time.Duration;

public class rds_test {
    static WebDriver driver;
    static By sign_inButton = By.xpath("//span[@id ='ui-id-7' and text()='Kering Auth']");
    static By username = By.xpath("//input[@name='identifier' and @autocomplete = 'username' and @type ='text']");
    static By login_button = By.xpath("//button[text()='Next' and @type='submit']");
    static By password = By.xpath("//input[@id='credentials.passcode' or @autocomplete='current-password']");
    static By verify = By.xpath("//button[text()='Verify' and @type='submit']");
    static By XOCS_login = By.xpath("//a [contains(@href,'GO_WORKSPACE') and text()='XOCS']");
    static By SQL_workshop = By.xpath("//span[text()='SQL Workshop']");
    static By SQL_Commands = By.xpath("//span[text()='SQL Commands']");
    static By starting_line = By.xpath("//div[@class='view-line']");

    static By Run_Button = By.xpath("//button[@id='B10644629905348428' and text()='Run']");
    static By download_results = By.xpath("//a[text()='Download']");

    static By clear_command = By.xpath("//a[text()='Clear Command' and @class='a-Button a-Button--small']");

    static String redeemableStoreCredit_Query = "select tv.voucher_typcode, tv.serial_nbr, tv.unspent_balance_amt, TO_CHAR(tv.create_date,'YYYY-MM-DD'), TO_CHAR(tv.expr_date,'YYYY-MM-DD')\n" +
            "from rds_ttr_voucher tv\n" +
            "join rds_ttr_voucher_history tvh on tv.serial_nbr=tvh.serial_nbr\n" +
            "where tv.voucher_typcode in ('STORE_CREDIT','RELATE_GIFT_CARD')\n" +
            "and tvh.activity_code = 'ISSUED'\n" +
            "and tvh.serial_nbr not in (select serial_nbr from rds_ttr_voucher_history where voucher_typcode in ('STORE_CREDIT')and activity_code = 'REDEEMED')\n" +
            "and tv.unspent_balance_amt >0\n" +
            "order by tv.expr_date, tvh.create_date desc, tv.voucher_typcode, tv.serial_nbr;";





    public static void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://rgbu.gbua.eu-frankfurt-1.oci.oraclecloud.com/eez9lnjmsm5a2n1c6ngh/ords");
        driver.findElement(sign_inButton).click();
        driver.findElement(username).sendKeys(login.Username);
        driver.findElement(login_button).click();
        driver.findElement(password).sendKeys(login.password);
        driver.findElement(verify).click();
        driver.findElement(XOCS_login).click();
        driver.findElement(SQL_workshop).click();
        driver.findElement(SQL_Commands).click();
    }


    public static void getredeemableStoreCredit(){
        driver.findElement(starting_line).click();
        WebElement starting_line_feild = driver.findElement(starting_line);
        Actions action = new Actions(driver);
        action.moveToElement(starting_line_feild).sendKeys(redeemableStoreCredit_Query).perform();

        // Run Query
        driver.findElement(Run_Button).click();

        JavascriptExecutor js = (JavascriptExecutor) driver;

        //Clear Query
        WebElement clear_command_button = driver.findElement(clear_command);
        js.executeScript("arguments[0].scrollIntoView(true);",clear_command_button);
        clear_command_button.click();

        //Download Results
        WebElement downloadData = driver.findElement(download_results);
        js.executeScript("arguments[0].scrollIntoView(true);", downloadData);
        downloadData.click();

    }

    public static void getTransactionData(){
        driver.findElement(starting_line).click();
        WebElement starting_line_feild = driver.findElement(starting_line);
        Actions action = new Actions(driver);
        action.moveToElement(starting_line_feild).sendKeys("select trl.rtl_loc_id, trl.wkstn_id, trl.create_user_id,trl.trans_seq, trl.business_date,trl.item_id, trl.quantity,trn.total,tndr.tndr_id,\n" +
                "inv.invoice_type, trl.SALE_LINEITM_TYPCODE,trl.RETURN_TYPCODE, trn.TRANS_STATCODE\n" +
                "from rds_trl_sale_lineitm trl\n" +
                "join rds_ttr_tndr_lineitm tndr on trl.trans_seq = tndr.trans_seq and trl.RTL_LOC_ID = tndr.RTL_LOC_ID\n" +
                "left join rds_civc_invoice inv on inv.INVOICE_TRANS_SEQ = trl.TRANS_SEQ and trl.RTL_LOC_ID = inv.RTL_LOC_ID\n" +
                "join rds_trn_trans trn on trl.trans_seq = trn.trans_seq and trl.RTL_LOC_ID = trn.RTL_LOC_ID\n" +
                "where trl.business_date like '" + rds_ops.business_data + "' and trl.RTL_LOC_ID ='"+rds_ops.store_num1 +
                "' and trl.WKSTN_ID='" + rds_ops.register_num+ "' order by trl.TRANS_SEQ desc ;").perform();

        // Run Query
        driver.findElement(Run_Button).click();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        //Clear Query
        WebElement clear_command_button = driver.findElement(clear_command);
        js.executeScript("arguments[0].scrollIntoView(true);",clear_command_button);
        clear_command_button.click();

        //Download results
        WebElement downloadData = driver.findElement(download_results);
        js.executeScript("arguments[0].scrollIntoView(true);", downloadData);
        downloadData.click();



    }



}
