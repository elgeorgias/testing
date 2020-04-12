package testing.selenium;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.codehaus.plexus.util.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class AmazonPrimeNow {

	public static String POSTALCODE = "x";
	public static String USER_APN = "xx";
	public static String PASS_APN = "xxx";

	public static void main(String[] args) {
		// setting the driver executable
		// macos
		System.setProperty("webdriver.chrome.driver", "resources/webdriver/macos/chromedriver");
		// windows
		// System.setProperty("webdriver.chrome.driver",
		// "resources/webdriver/win/chromedriver.exe");
		WebDriver driver = null;

		Boolean loopContinue = true;
		int triesCount = 0;
		int excepCount = 0;
		while (loopContinue) {

			try {
				// Initiating your chromedriver
				driver = new ChromeDriver();

				// Manage window
				// driver.manage().window().maximize();
				driver.manage().window().setPosition(new Point(-2000, 0));
				//driver.manage().window().setPosition(new Point(0, 0));
				// driver.manage().window().setSize(new Dimension(1680, 1027));
				// Applied wait time
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				// maximize window
				//driver.manage().window().maximize();

				// home page
				driver.get("https://primenow.amazon.es/home");

				// Postal Code
				driver.findElement(By.id("lsPostalCode")).click();
				driver.findElement(By.id("lsPostalCode")).sendKeys(POSTALCODE);
				driver.findElement(By.id("lsPostalCode")).sendKeys(Keys.ENTER);

				// Mi cuenta
				try {

//					WebElement el1 = driver
//							.findElement(By.xpath("./html/body/div[1]/div/header/div[1]/div[1]/div[1]/div/div[8]/a"));
					System.out.println("Element will be clicked... ");
					WebElement el1 = driver.findElement(By.cssSelector("div[data-reactid='89']"));

					el1.click();
				} catch (org.openqa.selenium.StaleElementReferenceException ex) {
					WebElement el1 = driver.findElement(By.cssSelector("div[data-reactid='89']"));
					System.out.println("Element will be clicked second... ");
					el1.click();
				}

				// login
				driver.findElement(By.id("ap_email")).sendKeys(USER_APN);
				driver.findElement(By.id("ap_password")).sendKeys(PASS_APN);
				driver.findElement(By.id("ap_password")).sendKeys(Keys.ENTER);

				// open cart
				WebElement el1 = driver.findElement(By.xpath(
						"./html/body/div[1]/div[1]/div[1]/div[1]/div/header/div[1]/div[1]/div/div/div[10]/a/span[2]"));
				System.out.println("Cart will be clicked... ");
				el1.click();

				// tramit order
				// driver.findElement(By.id("a-autoid-14-announce")).click();
				driver.findElement(By.linkText("Tramitar el pedido")).click();
				//select address
				//driver.findElement(By.cssSelector("div[data-testid='2']")).click();
				//driver.findElement(By.cssSelector("input[data-testid='pmts-primary-continue-button']")).click();

				WebElement we1 = driver.findElement(By.id("delivery-slot-form"));
				System.out.println("Try " + triesCount + ": " + we1.getText());

				if (we1.getText().equals(
						"Actualmente no hay ventanas de entrega disponibles para hoy ni mañana. Nuevas franjas de entrega se abren a lo largo de todo el día.")) {
					WebElement weContinuar = driver
							.findElement(By.id("delivery-slot-panel-continue-button-bottom-announce"));
					weContinuar.click();
					System.out.println("Try " + triesCount + ": " + weContinuar.getText());
					takeSnapShot(driver, "output/try" + triesCount + ".png");

					driver.close();
					triesCount++;
				} else {
					loopContinue = false;
					System.out.println("Final try " + triesCount);
					driver.manage().window().setPosition(new Point(0, 0));
					triesCount++;
					try {
						we1.click();
						WebElement weContinuar =
						 driver.findElement(By.id("delivery-slot-panel-continue-button-bottom-announce"));
						
						weContinuar.click();
					}
					catch (Exception e) {
							e.printStackTrace();
					}
					if (driver != null)
						driver.close();
					//takeSnapShot(driver, "output/tryStop" + triesCount + ".png");
					soundAlarm();
				}

			} catch (Exception e) {
				e.printStackTrace();
				if (driver != null)
					driver.close();
				excepCount++;
				System.out.println("Exception number:  " + excepCount);
//				try {
//					takeSnapShot(driver, "output/tryExcept" + excepCount + ".png");
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}


			}
		}
		// closing the browser
		// driver.close();
	}

	private static void soundAlarm() {
		try {
			Sequencer sequencer = MidiSystem.getSequencer();

			sequencer.open();

			Sequence sequence = new Sequence(Sequence.PPQ, 4);
			Track track = sequence.createTrack();
			ShortMessage a = new ShortMessage();
			a.setMessage(144, 9, 56, 100);
			MidiEvent event = new MidiEvent(a, 1);
			track.add(event);
			sequencer.setSequence(sequence);
			for (int i = 0; i <= 10; i++) {
				sequencer.start();
				Thread.sleep(500);
			}
			sequencer.close();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void takeSnapShot(WebDriver webdriver, String fileWithPath) throws Exception {
		// Convert web driver object to TakeScreenshot
		TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
		// Call getScreenshotAs method to create image file
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
		// Move image file to new destination
		File DestFile = new File(fileWithPath);
		// Copy file at destination
		FileUtils.copyFile(SrcFile, DestFile);
	}

}
