import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CaesarCipher {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите путь к файлу c текстом: ");
        String filePath = scanner.nextLine();

        System.out.print("Выберите действие: (1) Зашифровать (2) Расшифровать (3) Разломать: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        int key = -1;
        String wordMatch = "";
        if (choice != 3) {
            System.out.print("Введите криптографический ключ (целое число): ");
            key = scanner.nextInt();
        } else {
            System.out.print("Введите слово которое может содержаться в зашифрованном тексте: ");
            wordMatch = scanner.nextLine();
        }


        try {
            Scanner fileScanner = new Scanner(new File(filePath));
            String plaintext = fileScanner.useDelimiter("\\Z").next();
            fileScanner.close();
            if (choice == 1) {

                String encryptedText = encrypt(plaintext, key);

                byte[] contentBytes = encryptedText.getBytes();
                String newFileName = getNewFileName(filePath, "encrypted");
                Path path = Paths.get(newFileName);
                Files.write(path, contentBytes);

                System.out.println("Файл с зашифрованным текстом успешно создан. Путь к созданному файлу: " + path);
            } else if (choice == 2) {

                String decryptedText = decrypt(plaintext, key);

                byte[] contentBytes = decryptedText.getBytes();
                String newFileName = getNewFileName(filePath, "decrypted");
                Path path = Paths.get(newFileName);
                Files.write(path, contentBytes);

                System.out.println("Файл с расшифрованным текстом успешно создан. Путь к созданному файлу: " + path);
            } else if (choice == 3) {

                String newFileName = getNewFileName(filePath, "decrypted_brute_force");
                int bruteForceKey = getKeyByBruteForce(plaintext, wordMatch);
                String decryptedText = decrypt(plaintext, bruteForceKey);
                byte[] contentBytes = decryptedText.getBytes();
                Path path = Paths.get(newFileName);
                Files.write(path, contentBytes);
                System.out.println("Файл с расшифрованным текстом успешно создан. Путь к созданному файлу: " + path);
            } else {
                System.out.println("Неверный выбор. Пожалуйста, выберите (1) для шифрования, (2) или (3) для расшифровки.");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String text, int key) {
        StringBuilder encryptedText = new StringBuilder();
        String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя.,\":-!? ";
        for (char c : text.toCharArray()) {
            int index = alphabet.indexOf(c);
            if (index != -1) {
                int shiftedIndex = (index + key) % alphabet.length();
                encryptedText.append(alphabet.charAt(shiftedIndex));
            } else {
                encryptedText.append(c);
            }
        }
        return encryptedText.toString();
    }

    public static String decrypt(String encryptedText, int key) {
        StringBuilder decryptedText = new StringBuilder();
        String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя.,\":-!? ";
        for (char c : encryptedText.toCharArray()) {
            int index = alphabet.indexOf(c);
            if (index != -1) {
                int shiftedIndex = (index - key + alphabet.length()) % alphabet.length();
                decryptedText.append(alphabet.charAt(shiftedIndex));
            } else {
                decryptedText.append(c);
            }
        }
        return decryptedText.toString();
    }

    public static int getKeyByBruteForce(String encryptedText, String stringMacth) {
        int resultKey = -1;
        for (int key = 1; key <= 41; key++) {
            String decryptedText = decrypt(encryptedText, key);
            if (decryptedText.toLowerCase().contains(stringMacth.toLowerCase())) {
                resultKey = key;
            }
        }
        if (resultKey == -1) {
            throw new RuntimeException("Ключ не найден, попробуйте заново, используя другое слово.");
        }
        return resultKey;
    }

    public static String getNewFileName(String oldFileName, String suffix) {
        int dotIndex = oldFileName.lastIndexOf(".");
        return oldFileName.substring(0, dotIndex) + "_" + suffix + oldFileName.substring(dotIndex);
    }
}

