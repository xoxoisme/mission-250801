package com.back;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AppRepository {
    private final String path = "db/wiseSaying";

    //id.json에 명언 내용 쓰기
    public void save(int id, String quote, String author){
        String json = "{\n" +
                "   \"id\" : " + id + ",\n" +
                "   \"content\" : \"" + quote + "\",\n" +
                "   \"author\" : \"" + author + "\"\n" +
                "}";
        Path file = Path.of(path, id + ".json");
        try (FileWriter fw = new FileWriter(file.toFile())) {
            fw.write(json);
        } catch (IOException e) {
            System.out.println(id + ".json 파일 생성 실패");
        }
    }

    //마지막 등록한 명언 넘버 기록
    public void savaLastNum(int lastNum) {
        Path file = Path.of(path, "lastId.txt");
        try (FileWriter fw = new FileWriter(file.toFile())) {
            fw.write(String.valueOf(lastNum));
        } catch (IOException e) {
            System.out.println("lastId.json 파일 생성 실패");
        }
    }

    //모든 id.json 파일들을 data.json으로 병합
    public void buildAll(List<Wise> list) {
        try {
            StringBuilder json = new StringBuilder("[\n");
            FileWriter fw = new FileWriter("db/wiseSaying/data.json");
            for (int i = 0; i < list.size(); i++) {
                json.append("\t{\n")
                        .append("   \t\"id\" : ").append(list.get(i).getId()).append(",\n")
                        .append("   \t\"content\" : \"").append(list.get(i).getQuote()).append("\",\n")
                        .append("   \t\"author\" : \"").append(list.get(i).getAuthor()).append("\"\n")
                        .append("\t}");
                if (i < list.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append("]");
            fw.write(json.toString());
            fw.close();
        } catch (IOException e) {
            System.out.println("빌드 실패 : " + e.getMessage());
        }
    }

    //해당되는 id를 레포에서 삭제
    public void deleteFile(int id) {
        Path file = Path.of(path, id + ".json");
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            System.out.println("삭제 실패 : " + id);
        }
    }

    public int loadLastId() {
        Path file = Path.of(path, "lastId.txt");
        if (Files.exists(file)) {
            try {
                String lastIdTxt = Files.readString(file);
                return Integer.parseInt(lastIdTxt);
            } catch (IOException e) {
                System.out.println("lastId.txt 불러오기 실패");
            }
        }
        return 0;
    }

    //영속성 부여를 위한 이전 데이터 불러오기
    public List<Wise> loadAll() {
        List<Wise> list = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) return list;

        File[] files = file.listFiles((d, name) -> name.endsWith(".json") && !name.equals("data.json"));
        for (File f : files) {
            try {
                String inFile = Files.readString(f.toPath());
                inFile = inFile.replace("{", "").replace("}", "").replace("\"", "")
                        .replace(",", "").replace("id :","").replace("content : ", "")
                        .replace("author :", "").trim();    //파일 내 문자들 모두 빈공간으로 대체(이후 split 효율적으로 하기 위해)

                String[] res = inFile.split("\n");
                for (int i = 0; i < res.length; i++) res[i] = res[i].trim();    //공백문자 지우기
                int id = Integer.parseInt(res[0]);
                String quote = res[1];
                String author = res[2];

                list.add(new Wise(id, quote, author));
            } catch (IOException e) {
                System.out.println(f.getName() + ".json 불러오기 실패");
            }
        }
        return list;
    }
}
