package book.manage;

import book.manage.entity.Book;
import book.manage.entity.Student;
import book.manage.sql.SqlUtil;
import lombok.extern.java.Log;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.io.Resources;
import sun.util.logging.resources.logging;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.LogManager;

@Log
public class Main {
    public static void main(String[] args)  {


        try (Scanner scanner=new Scanner(System.in)){
            LogManager Manager=LogManager.getLogManager();
            Manager.readConfiguration(Resources.getResourceAsStream("logging.properties"));

            while (true) {
                System.out.println("====================");
                System.out.println("1.录入学生信息");
                System.out.println("2.录入书籍信息");
                System.out.println("3.学生借阅书籍信息");
                System.out.println("4.查询学生借阅书籍信息");
                System.out.println("5.查询学生信息");
                System.out.println("6.查询书籍信息");
                System.out.println("输入你想要的操作，其他任意数字退出");
                int input;
                try {
                    input=scanner.nextInt();
                }catch (Exception e){
                    return;
                }
                scanner.nextLine();
                switch (input){
                    case 1:
                        addStudent(scanner);
                        break;
                    case 2:
                        addBook(scanner);
                        break;
                    case 3:
                        addBorrow(scanner);
                        break;
                    case 4:
                        showBorrow();
                        break;
                    case 5:
                        showStudent();
                        break;
                    case 6:
                        showBook();
                        break;
                    default:
                        return;
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    private static void showBook(){
        SqlUtil.doSqlWork(mapper->{
            mapper.getBookList().forEach(System.out::println);
        });
    }
    private static void showStudent(){
        SqlUtil.doSqlWork(mapper->{
            mapper.getStudentList().forEach(System.out::println);
        });
    }
    private static void showBorrow(){
        SqlUtil.doSqlWork(mapper->{
            mapper.getBorrowList().forEach(borrow -> {
                System.out.println(borrow.getStudent().getName()+"->"+borrow.getBook().getTitle());
            });
        });
    }
    private static void addBorrow(Scanner scanner){
        System.out.println("请输入书籍号:");
        String a=scanner.nextLine();
        int bid=Integer.parseInt(a);
        System.out.println("请输入学号");
        String b=scanner.nextLine();
        int sid=Integer.parseInt(b);
        SqlUtil.doSqlWork(mapper->{
            int i=mapper.addBorrow(sid,bid);
            if(i>0) {
                System.out.println("借阅信息录入成功");
                log.info("新添加一条借阅记录"+sid+bid);
            }
            else System.out.println("录入失败");
        });
    }

    private static void addBook(Scanner scanner){
        System.out.print("请输入书籍名字：");
        String title=scanner.nextLine();
        System.out.print("请输入书籍介绍：");
        String desc=scanner.nextLine();
        System.out.print("请输入书籍价格：");
        String price=scanner.nextLine();
        double p= Double.parseDouble(price);
        Book book=new Book(title,desc,p);
        SqlUtil.doSqlWork(mapper ->{ //lambda表达式,传入一个函数作为参数
            int i=mapper.addBook(book);
            if(i>0) {
                System.out.println("书籍信息录入成功");
                log.info("新添加一条书籍信息"+book);
            }
            else System.out.println("录入失败");
        });
    }


    private static void addStudent(Scanner scanner){
        String sex;
        System.out.print("请输入学生姓名：");
        String name=scanner.nextLine();
        System.out.print("请输入学生性别：");
        while(true){
            sex=scanner.nextLine();
            if (sex.equals("男")||sex.equals("女")) break;
            else System.out.print("请重新输入：");
        }
        System.out.print("请输入学生年级：");
        String grade=scanner.nextLine();
        int g= Integer.parseInt(grade);
        Student student=new Student(name,sex,g);
        SqlUtil.doSqlWork(mapper ->{
            int i=mapper.addStudent(student);
            if(i>0){
                System.out.println("学生信息录入成功");
                log.info("新添加一条学生信息"+student);
            }
            else System.out.println("录入失败");
        });
    }
}
