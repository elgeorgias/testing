package testing.selenium;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class Filmafinity {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// setting the driver executable
		System.setProperty("webdriver.chrome.driver", "/Users/jruano/Desktop/temp/selenium_IDE/webdriver/chromedriver");

		// Initiating your chromedriver
		WebDriver driver = new ChromeDriver();

		// Applied wait time
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		// maximize window
		driver.manage().window().maximize();

		// open browser with desired URL
		BufferedReader reader;
		try {
			// output
			File newFile = new File("resources/movies_rates.csv");
			// input
			//reader = new BufferedReader(new FileReader("resources/clean_list.txt"));
			reader = new BufferedReader(new FileReader("resources/clean_list.txt"));

			String line = reader.readLine();
			int startLine = 6;
			int count = 0;
			while (line != null) {
				if (count >= startLine) {
					driver.get("https://www.filmaffinity.com/en/tours.php");
					String titleFilm = line;
					// System.out.println("Title -> " + titleFilm);
					driver.findElement(By.id("top-search-input")).sendKeys(titleFilm);
					try {					
						WebElement element = driver.findElement(By.cssSelector("#ui-id-2 > .title"));
						if (element != null) {
							element.click();
							driver.findElement(By.id("movie-rat-avg")).click();
							String contentVal = driver.findElement(By.id("movie-rat-avg")).getAttribute("content")
									.toString();
							String newLine = titleFilm + " = " + contentVal;
							System.out.println(newLine);
							addNewLineFile(newFile, newLine);
						}
					} catch (NoSuchElementException e) {
						// e.printStackTrace();
						String newLine = titleFilm + " = " + "NotSuchElement";
						System.out.println(newLine);
						addNewLineFile(newFile, newLine);
					} catch (WebDriverException e) {
						// e.printStackTrace();
						String newLine = titleFilm + " = " + "WebDriverConnection";
						System.out.println(newLine);
						addNewLineFile(newFile, newLine);
					}
				}
				count++;
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("Exception");
			driver.close();

		}
		// closing the browser
		driver.close();
	}

	private static void addNewLineFile(File fileFilms, String newLine) {
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(fileFilms, true));
			if (output!=null) {
				output.append(newLine + "\n");
				output.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
