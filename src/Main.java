import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static Map<String, String> slangWords;
    private static List<String> searchHistory;

    private static String bulkhead;

    public static void main(String[] args) {
        initSlangWord(false);

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
                case 6:
                    deleteSlangWord(scanner);
                    break;
                case 7:
                    resetSlangWords(scanner);
                    break;
                case 8:
                    randomSlangWord();
                    break;
                case 9:
                    quizSlangWord(scanner);
                    break;
                case 10:
                    quizDefinition(scanner);
                    break;
                case 0:
                    System.out.println("Bạn đã tắt ứng dụng. Hẹn gặp lại!");
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
        System.out.println("6. Xóa slang word");
        System.out.println("7. Reset danh sách slang word");
        System.out.println("8. Random slang word");
        System.out.println("9. Đố vui slang word");
        System.out.println("10. Đố vui definition");
        System.out.println("0. Thoát");
        System.out.print("Nhập lựa chọn của bạn: ");
    }

    private static void initSlangWord(Boolean isReset) {
        slangWords = new HashMap<>();
        searchHistory = new ArrayList<>();

        String filePath = "slang.txt"; // Đường dẫn tới file
        if (!isReset) {
            System.out.print("Nhập kí tự phân tách (không nhập mặc định là \""+"`"+"\"): ");
            Scanner scanner = new Scanner(System.in);
            String temp = scanner.nextLine();
            boolean valid = temp != null && !temp.isEmpty() && !temp.trim().isEmpty();
            if (valid) {
                bulkhead = temp;
            } else {
                bulkhead = "`";
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(bulkhead);
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

    private static void deleteSlangWord(Scanner scanner) {
        System.out.print("Nhập slang word cần xóa: ");
        String slangWord = scanner.nextLine();

        if (slangWords.containsKey(slangWord)) {
            System.out.print("Bạn có chắc chắn muốn xóa slang word \"" + slangWord + "\" không? (y/n): ");
            String choose = scanner.nextLine();

            if (choose.equalsIgnoreCase("y") || choose.equalsIgnoreCase("Y") || choose.equalsIgnoreCase("yes")) {
                slangWords.remove(slangWord);
                System.out.println("Slang word đã được xóa thành công!");
            } else if (choose.equalsIgnoreCase("n") || choose.equalsIgnoreCase("N") || choose.equalsIgnoreCase("no")) {
                System.out.println("Không xóa slang word.");
            } else {
                System.out.println("Lựa chọn của bạn không hợp lệ. Vui lòng thử lại sau!");
            }
        } else {
            System.out.println("Rất tiếc. Không tìm thấy slang word này trong từ điển. Vui lòng thử lại sau!");
        }
    }

    private static void resetSlangWords(Scanner scanner) {
        initSlangWord(true);
        System.out.print("Bạn có chắc chắn muốn reset không? (y/n): ");
        String choose = scanner.nextLine();

        if (choose.equalsIgnoreCase("y") || choose.equalsIgnoreCase("Y") || choose.equalsIgnoreCase("yes")) {
            System.out.println("Đã reset danh sách slang words thành công!");
        } else if (choose.equalsIgnoreCase("n") || choose.equalsIgnoreCase("N") || choose.equalsIgnoreCase("no")) {
            System.out.println("Không reset slang word.");
        } else {
            System.out.println("Lựa chọn của bạn không hợp lệ. Vui lòng thử lại sau!");
        }
    }

    private static void randomSlangWord() {
        List<String> slangWordList = new ArrayList<>(slangWords.keySet());
        Random random = new Random();
        int randomIndex = random.nextInt(slangWordList.size());
        String randomSlangWord = slangWordList.get(randomIndex);

        System.out.println("Slang word ngẫu nhiên: " + randomSlangWord);
        System.out.println("Definition: " + slangWords.get(randomSlangWord));
    }

    private static void quizSlangWord(Scanner scanner) {
        List<String> listSlangWord = new ArrayList<>(slangWords.keySet());
        Random random = new Random();
        int rIndex = random.nextInt(listSlangWord.size());
        String quizSlangWord = listSlangWord.get(rIndex);
        System.out.println("Hãy chọn definition cho slang word \"" + quizSlangWord + "\": ");

        List<String> options = generateQuestionOptions(quizSlangWord);
        Collections.shuffle((options));

        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }

        int choose = scanner.nextInt();

        if (choose >= 1 && choose <= options.size()) {
            String answer = options.get(choose - 1);
            if (answer.equals(slangWords.get(quizSlangWord))) {
                System.out.println("Chính xác! Bạn đã trả lời đúng.");
            } else {
                System.out.println("Rất tiếc. Câu trả lời chưa chính xác.");
                System.out.println("Đáp án đúng là \"" + slangWords.get(quizSlangWord) + "\".");
            }
        } else {
            System.out.println("Lựa chọn của bạn không hợp lệ");
        }
    }

    private static List<String> generateQuestionOptions(String quizSlangWord) {
        List<String> answerOptions = new ArrayList<>();
        // Đáp án đúng
        answerOptions.add(slangWords.get(quizSlangWord));

        // 3 Đáp án ngẫu nhiên khác
        List<String> listSlangWord = new ArrayList<>(slangWords.keySet());
        listSlangWord.remove(quizSlangWord);

        // Trộn ngẫu nhiên danh sách tạm thời
        Collections.shuffle(listSlangWord);

        int numOptions = Math.min(listSlangWord.size(), 3);
        for (int i = 0; i < numOptions; i++) {
            answerOptions.add(slangWords.get(listSlangWord.get(i)));
        }

        return answerOptions;
    }

    private static void quizDefinition(Scanner scanner) {
        List<String> listDefinition = new ArrayList<>(slangWords.values());
        Random random = new Random();
        int rIndex = random.nextInt(listDefinition.size());
        String quizDefinition = listDefinition.get(rIndex);
        System.out.println("Chọn slang word cho definition \"" + quizDefinition + "\": ");
        List<String> answerOptions = generateAnswerOptions(quizDefinition);
        Collections.shuffle(answerOptions);

        for (int i = 0; i < answerOptions.size(); i++) {
            System.out.println((i + 1) + ". " + answerOptions.get(i));
        }

        int userChoice = scanner.nextInt();

        if (userChoice >= 1 && userChoice <= answerOptions.size()) {
            String userAnswer = answerOptions.get(userChoice - 1);
            String correctSlangWord = getSlangWord(quizDefinition);

            if (userAnswer.equals(correctSlangWord)) {
                System.out.println("Chính xác! Bạn đã trả lời đúng.");
            } else {
                System.out.println("Sai rồi! Đáp án đúng là \"" + correctSlangWord + "\".");
            }
        } else {
            System.out.println("Lựa chọn không hợp lệ.");
        }
    }

    private static List<String> generateAnswerOptions(String quizSlangWord) {
        List<String> answerOptions = new ArrayList<>();
        // Đáp án đúng
        answerOptions.add(getSlangWord(quizSlangWord));

        // 3 Đáp án ngẫu nhiên khác
        List<String> listSlangWord = new ArrayList<>(slangWords.keySet());
        listSlangWord.remove(quizSlangWord);

        // Trộn ngẫu nhiên danh sách tạm thời
        Collections.shuffle(listSlangWord);

        int numOptions = Math.min(listSlangWord.size(), 3);
        for (int i = 0; i < numOptions; i++) {
            answerOptions.add(slangWords.get(listSlangWord.get(i)));
        }

        return answerOptions;
    }

    private static String getSlangWord(String definition) {
        for (Map.Entry<String, String> entry : slangWords.entrySet()) {
            if (entry.getValue().equals(definition)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
