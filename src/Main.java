import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static Map<String, String> slangWords;
    private static List<String> searchHistory;

    public static void main(String[] args) {
        initializeSlangDictionary();

        Scanner scanner = new Scanner(System.in);
        int choose = 0;

        do {
            displayMenu();
            choose = scanner.nextInt();
            scanner.nextLine();

            switch (choose) {
                case 1:
                    searchBySlangWord(scanner);
                    break;
                case 2:
                    searchByDefinition(scanner);
                    break;
                case 3:
                    displaySearchHistory();
                    break;
                case 4:
                    addonSlangWord(scanner);
                    break;
                case 5:
                    editSlangWord(scanner);
                    break;
                case 8:
                    displayRandomSlangWord();
                    break;
                case 0:
                    System.out.println("Cảm ơn bạn đã sử dụng ứng dụng Slang Dictionary. Hẹn gặp lại!");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    break;
            }

            System.out.println();
        } while (choose != 0);
    }

    private static void displayMenu() {
        System.out.println("======= Slang Word Dictionary =======");
        System.out.println("1. Tìm kiếm theo slang word");
        System.out.println("2. Tìm kiếm theo definition");
        System.out.println("3. Hiển thị lịch sử tìm kiếm");
        System.out.println("4. Thêm mới slang word");
        System.out.println("5. Chỉnh sửa slang word");
        System.out.println("8. Random slang word");
        System.out.println("0. Thoát");
        System.out.print("Nhập lựa chọn của bạn: ");
    }

    private static void initializeSlangDictionary() {
        slangWords = new HashMap<>();
        searchHistory = new ArrayList<>();

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
            addToSearchHistory(slangWord);
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

    private static void displaySearchHistory() {
        if (searchHistory.isEmpty()) {
            System.out.println("Lịch sử tìm kiếm trống.");
        } else {
            System.out.println("Các slang words đã tìm kiếm:");
            for (String slangWord : searchHistory) {
                System.out.println("- " + slangWord);
            }
        }
    }

    private static void addToSearchHistory(String slangWord) {
        searchHistory.add(slangWord);
    }

    private static void addonSlangWord(Scanner scanner) {
        System.out.print("Nhập slang word muốn thêm: ");
        String slangWord = scanner.nextLine();

        if (slangWords.containsKey(slangWord)) {
            System.out.print("Slang word đã tồn tại. Bạn có muốn ghi đè (overwrite) hay tạo slang word mới (duplicate)? (o/d): ");
            String choose = scanner.nextLine();

            if (choose.equalsIgnoreCase("o")) {
                System.out.print("Nhập definition mới cho slang word \"" + slangWord + "\": ");
                String newDefinition = scanner.nextLine();
                slangWords.put(slangWord, newDefinition);
                System.out.println("Slang word đã được ghi đè thành công!");
            } else if (choose.equalsIgnoreCase("d")) {
                System.out.println("Không ghi đè, tạo slang word trùng.");
                addNewSlangWord(scanner, slangWord);
            } else {
                System.out.println("Lựa chọn không hợp lệ");
            }
        } else {
            addNewSlangWord(scanner, slangWord);
        }
    }

    private static void addNewSlangWord(Scanner scanner, String slangWord) {
        System.out.print("Nhập definition cho slang word \"" + slangWord + "\": ");
        String definition = scanner.nextLine();
        slangWords.put(slangWord, definition);
        System.out.println("Slang word mới đã được thêm thành công!");
    }

    private static void editSlangWord(Scanner scanner) {
        System.out.print("Nhập slang word cần chỉnh sửa: ");
        String slangWord = scanner.nextLine();

        if (slangWords.containsKey(slangWord)) {
            System.out.print("Nhập definition mới cho slang word \"" + slangWord + "\": ");
            String newDefinition = scanner.nextLine();
            slangWords.put(slangWord, newDefinition);
            System.out.println("Slang word đã được chỉnh sửa thành công.");
        } else {
            System.out.println("Không tìm thấy slang word này trong từ điển.");
        }
    }

    private static void displayRandomSlangWord() {
        List<String> slangWordList = new ArrayList<>(slangWords.keySet());
        System.out.println(slangWordList);
        Random random = new Random();
        int randomIndex = random.nextInt(slangWordList.size());
        String randomSlangWord = slangWordList.get(randomIndex);

        System.out.println("Slang word ngẫu nhiên: " + randomSlangWord);
        System.out.println("Definition: " + slangWords.get(randomSlangWord));
    }
}
