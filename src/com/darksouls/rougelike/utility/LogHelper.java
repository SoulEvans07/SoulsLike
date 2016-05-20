package com.darksouls.rougelike.utility;

import com.darksouls.rougelike.references.Reference;

import java.io.PrintStream;

/**  Log Helper v1.0  **/
public class LogHelper {
    private static PrintStream printStream = System.out;
    private static int mute = 0;
    private static int comm = 0;
    private static int paragraph = 0;

    //----------Primitive---------------
    private static void log(String log){
        if(mute == 0)
            printStream.print(log);
    }

    //----------Writers-----------------
    public static void write(Object line){
        String raw = String.valueOf(line);
        String fin = paragrapher();
        String end = "";
        if(raw.endsWith("\n")){
            raw = raw.substring(0, raw.length() - 1);
            end = "\n";
        }
        raw = raw.replace("\n ", "\n"); // I like to place a space after '\n' for readability
        raw = raw.replace("\n", "\n" + paragrapher());
        fin = fin.concat(raw + end);
        log(fin);
    }

    public static void writeLn(Object line){
        String raw = String.valueOf(line);
        String fin = paragrapher();
        String end = "\n";
        if(raw.endsWith("\n")){
            raw.substring(0, raw.length() - 1);
            end = end.concat("\n");
        }
        raw = raw.replace("\n ", "\n"); // I like to place a space after '\n' for readability
        raw = raw.replace("\n", "\n" + paragrapher());
        fin = fin.concat(raw + end);
        log(fin);
    }

    public static void inLine(Object line){
        String raw = String.valueOf(line);
        String fin = paragrapher();
        raw = raw.replace("\n", " ");
        fin = fin.concat(raw);
        log(fin);
    }

    public static void comment(Object comment){
        String raw = String.valueOf(comment);
        raw = raw.replace("\n", "; ");
        if(comm == 0)
            log("[Comment]" + tabber() + raw + '\n');
    }

    public static void error(Object error){
        String raw = String.valueOf(error);
        String tag = "[ERROR] ";
        tag = tag.concat(paragrapher(tag.length()));
        log(tag + raw + '\n');
    }

    //----------Controllers-------------
    public static void mute(){
        mute++;
    }

    public static void unMute(){
        mute--;
        if(mute < 0)
            LogHelper.error("mute depth below zero: " + mute);
    }

    public static void muteCom(){
        comm++;
    }

    public static void unMuteComm(){
        comm--;
        if(comm < 0)
            LogHelper.error("comment mute depth below zero: " + comm);
    }

    public static void lift(){
        lift(1);
    }

    public static void lift(int num){
        paragraph += num;
    }

    public static void close(){
        close(1);
    }

    public static void close(int num){
        paragraph -= num;
        if(paragraph < 0)
            LogHelper.error("paragraph depth below zero: " + paragraph);
    }

    //----------I/O---------------------
    public static void setStream(PrintStream ps){
        if(ps != null)
            printStream = ps;
        else
            LogHelper.error("given stream is NULL!");
    }

    //----------Reset-------------------
    public static void reset(){
        mute = 0;
        comm = 0;
        paragraph = 0;
        printStream = System.out;
    }

    //----------Paragraps---------------
    private static String paragrapher(int before){
        String tab = "";
        for(int i = 1; i < paragraph; i++)
            tab = tab.concat(LogHelper.tabber());
        if(before < tab.length())
            tab = tab.substring(before);
        return tab;
    }
    private static String paragrapher(){
        String tab = "";
        for(int i = 0; i < paragraph; i++)
            tab = tab.concat(LogHelper.tabber());
        return tab;
    }

    public static void breakLine(){
        breakLine(1);
    }

    public static void breakLine(int breaks){
        for(int i = 0; i < breaks; i++)
            log("\n");
    }

    public static String tabber(){
        return tabber(Reference.TAB_SIZE);
    }

    public static String tabber(int tabSize){
        String tab = "";
        for(int i = 0; i < tabSize; i++){
            tab = tab.concat(" ");
        }

        return tab;
    }
}
