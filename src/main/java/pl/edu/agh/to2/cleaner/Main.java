package pl.edu.agh.to2.cleaner;

import javafx.application.Application;
import pl.edu.agh.to2.cleaner.gui.AppController;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.effect.Rename;

import java.io.File;
import java.io.IOException;


public class Main {
	public static void main(String[] args) {
//		String filePath = "C:/Users/Maksymilian Katolik/Desktop/TO lab/to-project/example_dir/plik1.txt";
//
//		String newFileName = "zmieniony_plik1.txt";
//
//		File file = new File(filePath);
//
//		FileInfo fileInfo = new FileInfo(file);
//
//		System.out.println("Przed zmianą nazwy:");
//		System.out.println(fileInfo.describe());
//
//		Rename renameEffect = new Rename(fileInfo, newFileName);
//
//		try {
//			renameEffect.apply();
//			System.out.println("\nPo zmianie nazwy:");
//			System.out.println(fileInfo.describe());
//		} catch (IOException e) {
//			System.err.println("Błąd podczas zmiany nazwy: " + e.getMessage());
//		}

		Application.launch(App.class);


	}
}
