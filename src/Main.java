import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static Map<String, String> slangWords;

    public static void main(String[] args) {
        initializeSlangDictionary();

        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        do {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Đọc bỏ ký tự xuống dòng sau lệnh nextInt()

            switch (choice) {
                case 1:
                    searchBySlangWord(scanner);
                    break;
                case 2:
                    searchByDefinition(scanner);
                    break;
                case 0:
                    System.out.println("Cảm ơn bạn đã sử dụng ứng dụng Slang Dictionary. Hẹn gặp lại!");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    break;
            }

            System.out.println();
        } while (choice != 0);
    }

    private static void displayMenu() {
        System.out.println("------ Slang Dictionary ------");
        System.out.println("1. Tìm kiếm theo slang word");
        System.out.println("2. Tìm kiếm theo definition");
        System.out.println("0. Thoát");
        System.out.print("Nhập lựa chọn của bạn: ");
    }

    private static void initializeSlangDictionary() {
        slangWords = new HashMap<>();

        String filePath = "slang.txt"; // Đường dẫn tới file

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("`");
                if (parts.length == 2) {
                    String slang = parts[0].trim();
                    String meaning = parts[1].trim();
                    slangWords.put(slang, meaning);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void searchBySlangWord(Scanner scanner) {
        System.out.print("Nhập slang word cần tìm kiếm: ");
        String slangWord = scanner.nextLine();

        if (slangWords.containsKey(slangWord)) {
            String definition = slangWords.get(slangWord);
            System.out.println("Slang word: " + slangWord);
            System.out.println("Definition: " + definition);
        } else {
            System.out.println("Không tìm thấy slang word này trong từ điển.");
        }
    }

    private static void searchByDefinition(Scanner scanner) {
        System.out.print("Nhập từ khóa để tìm kiếm definition: ");
        String keyword = scanner.nextLine();

        List<String> foundSlangWords = new ArrayList<>();

        for (Map.Entry<String, String> entry : slangWords.entrySet()) {
            String slangWord = entry.getKey();
            String definition = entry.getValue();

            if (definition.toLowerCase().contains(keyword.toLowerCase())) {
                foundSlangWords.add(slangWord);
            }
        }

        if (!foundSlangWords.isEmpty()) {
            System.out.println("Các slang words có definition chứa từ khóa \"" + keyword + "\":");
            for (String slangWord : foundSlangWords) {
                System.out.println("- " + slangWord);
            }
        } else {
            System.out.println("Không tìm thấy slang words nào có definition chứa từ khóa \"" + keyword + "\".");
        }
    }
}
