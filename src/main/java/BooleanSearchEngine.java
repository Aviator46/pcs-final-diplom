import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> wordMap = new HashMap<>(); //индексация
    private File pdfsDir;


    public BooleanSearchEngine(File pdfsDir) throws IOException {
        File[] pdfsList = pdfsDir.listFiles(); //массив с файлами для поиска
        if (pdfsList == null) {
            throw new IOException("Файлы для поиска отсутствуют");
        } else {
            for (File pdf : pdfsList) { //перебираем все файлы
                var doc = new PdfDocument(new PdfReader(pdf)); //работаем с файлом
                for (int page = 1; page < doc.getNumberOfPages(); page++) { //перебираем все страницы
                    var text = PdfTextExtractor.getTextFromPage(doc.getPage(page)); //получаем текст со страницы
                    var words = text.split("\\P{IsAlphabetic}+"); //разбиваем текст на слова

                    Map<String, Integer> freqs = new HashMap<>(); //считаем частоту слов
                    for (var word : words) {
                        if (word.isEmpty()) {
                            continue;
                        }
                        word = word.toLowerCase();
                        freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                    }
                    for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                        if (wordMap.containsKey(entry.getKey())) {
                            wordMap.get(entry.getKey()).add(new PageEntry(pdf.getName(), page, entry.getValue()));
                        } else {
                            wordMap.put(entry.getKey(), new ArrayList<>());
                            wordMap.get(entry.getKey()).add(new PageEntry(pdf.getName(), page, entry.getValue()));
                        }
                    }
                }
            }
        }
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        for (Map.Entry<String, List<PageEntry>> map : wordMap.entrySet()) {
            if (map.getKey().equals(word.toLowerCase())){
                List<PageEntry> result = map.getValue();
                Collections.sort(result);
                return result;
            }
        }
        return Collections.emptyList();
    }
}