package com.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppController {

    private Scanner sc;
    private List<Wise> list = new ArrayList<>();
    private final AppRepository repo = new AppRepository();
    private int id = 1;

    public AppController(Scanner sc) {
        this.sc = sc;
    }

    public void loadData() {
        this.list = repo.loadAll();
        this.id = repo.loadLastId() + 1;
    }
    //각 명령어 별 역할 분류
    public void command(String cmd) {
        if (cmd.equals("등록")) {
            register();
        } else if (cmd.equals("목록")) {
            showList();
        } else if (cmd.startsWith("삭제?id=")) {
            delete(cmd);
        } else if (cmd.startsWith("수정?id=")) {
            update(cmd);
        } else if (cmd.equals("빌드")) {
            build(list);
        }
    }

    //명언 등록
    public void register() {
        System.out.print("명언 : ");
        String quote = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();
        repo.save(id, quote, author);   //id.json 저장
        repo.savaLastNum(id);   //lastId.json 저장
        list.add(new Wise(id, quote, author));
        System.out.println(id++ + "번 명언이 등록되었습니다.");
    }

    //명언 목록
    public void showList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (int i = list.size() - 1; i >= 0; i--) {
            System.out.println(list.get(i).getId() + " / " + list.get(i).getAuthor() + " / " + list.get(i).getQuote());
        }
    }

    // 명언 삭제
    public void delete(String cmd) {
        String ids = cmd.substring("삭제?id=".length());
        int id = Integer.parseInt(ids);
        boolean find = false;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {  //리스트에서 해당되는 id 명언 삭제
                list.remove(i); //리스트 순서 중 i번째 삭제
                repo.deleteFile(id);    //파일은 id로 삭제
                System.out.println(id + "번 명언이 삭제되었습니다.");
                find = true;
                break;
            }
        }
        if (!find) System.out.println(id + "번 명언은 존재하지 않습니다.");
    }

    //명언 수정
    public void update(String cmd) {
        String ids = cmd.substring("수정?id=".length());
        int id = Integer.parseInt(ids);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id){
                System.out.println("명언(기존) : " + list.get(i).getQuote());
                System.out.print("명언 : ");
                list.get(i).setQuote(sc.nextLine());
                System.out.println("작가(기존) : " + list.get(i).getAuthor());
                System.out.print("작가 : ");
                list.get(i).setAuthor(sc.nextLine());
                break ;
            }
            else {
                System.out.println(id + "번 명언은 존재하지 않습니다.");
                break ;
            }
        }
    }

    //전체 명언 내용 최신화
    public void build(List<Wise> list) {
        repo.buildAll(list);
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}
