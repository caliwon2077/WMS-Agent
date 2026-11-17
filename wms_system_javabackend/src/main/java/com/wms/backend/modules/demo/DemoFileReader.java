package com.wms.backend.modules.demo;

import static com.wms.backend.modules.demo.DemoJson.*;
import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;
import java.util.zip.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/** Bounded CSV / OOXML reader. No macros, formulas, external relationships or XML entity resolution. */
public final class DemoFileReader {
    private DemoFileReader(){}
    public static List<List<String>> read(String filename,byte[] bytes){
        require(bytes.length>0&&bytes.length<=5*1024*1024,"文件须为 1 字节至 5 MiB");
        try{
            List<List<String>> result;
            if(filename.toLowerCase(Locale.ROOT).endsWith(".csv"))result=csv(StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).decode(ByteBuffer.wrap(bytes)).toString());
            else if(filename.toLowerCase(Locale.ROOT).endsWith(".xlsx"))result=xlsx(bytes);
            else throw bad("UNSUPPORTED_FILE","仅支持 .xlsx 和 UTF-8 .csv，不支持 xls 或宏文件");
            long nonempty=result.stream().filter(r->!r.stream().allMatch(String::isEmpty)).count();
            require(nonempty>=2,"文件需要表头和至少一行数据");require(nonempty<=1001,"一次最多导入 1000 行数据");return result;
        }catch(com.wms.backend.common.exception.BusinessException e){throw e;}catch(Exception e){throw bad("INVALID_FILE","文件无法解析：请使用未加密的标准 xlsx 或 UTF-8 CSV");}
    }
    static List<List<String>> csv(String text){
        if(text.startsWith("\uFEFF"))text=text.substring(1);
        List<List<String>> out=new ArrayList<>();List<String> row=new ArrayList<>();StringBuilder cell=new StringBuilder();boolean quoted=false,closed=false;
        for(int i=0;i<text.length();i++){
            char c=text.charAt(i);
            if(quoted){if(c=='"'){if(i+1<text.length()&&text.charAt(i+1)=='"'){cell.append('"');i++;}else{quoted=false;closed=true;}}else cell.append(c);}
            else if(c=='"'){require(cell.length()==0&&!closed,"CSV 引号格式错误");quoted=true;}
            else if(c==','||c=='\n'||c=='\r'){row.add(cell.toString().trim());cell.setLength(0);closed=false;if(c!=','){out.add(row);row=new ArrayList<>();if(c=='\r'&&i+1<text.length()&&text.charAt(i+1)=='\n')i++;}}
            else {require(!closed,"CSV 引号后存在非法字符");cell.append(c);}
            require(cell.length()<=10000&&row.size()<=20&&out.size()<=10000,"文件超过字段或物理行数限制");
        }
        require(!quoted,"CSV 引号未闭合");if(cell.length()>0||!row.isEmpty()){row.add(cell.toString().trim());out.add(row);}return out;
    }
    static Document xml(byte[] bytes)throws Exception{
        DocumentBuilderFactory f=DocumentBuilderFactory.newInstance();f.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);f.setFeature("http://xml.org/sax/features/external-general-entities",false);f.setFeature("http://xml.org/sax/features/external-parameter-entities",false);f.setXIncludeAware(false);f.setExpandEntityReferences(false);return f.newDocumentBuilder().parse(new ByteArrayInputStream(bytes));
    }
    private static List<List<String>> xlsx(byte[] bytes)throws Exception{
        Map<String,byte[]> files=new HashMap<>();int total=0,entries=0;
        try(ZipInputStream in=new ZipInputStream(new ByteArrayInputStream(bytes))){ZipEntry e;while((e=in.getNextEntry())!=null){
            require(++entries<=500,"工作簿内部文件过多");String n=e.getName();require(!n.contains("..")&&!n.toLowerCase(Locale.ROOT).contains("vbaproject"),"不支持宏或非法工作簿路径");
            ByteArrayOutputStream b=new ByteArrayOutputStream();byte[] buf=new byte[8192];int len;while((len=in.read(buf))!=-1){total+=len;require(total<=25*1024*1024,"工作簿解压大小超过 25 MiB");b.write(buf,0,len);}files.put(n,b.toByteArray());
        }}
        require(files.containsKey("xl/workbook.xml"),"不是有效的 xlsx 工作簿");
        for(Map.Entry<String,byte[]> e:files.entrySet())if(e.getKey().endsWith(".rels")){NodeList rels=xml(e.getValue()).getElementsByTagName("Relationship");for(int i=0;i<rels.getLength();i++)require(!((Element)rels.item(i)).getAttribute("TargetMode").equals("External"),"不支持外部链接");}
        NodeList sheets=xml(files.get("xl/workbook.xml")).getElementsByTagName("sheet");Element selected=null;
        for(int i=0;i<sheets.getLength();i++){Element s=(Element)sheets.item(i);if(sheets.getLength()==1||s.getAttribute("name").equals("设备导入"))selected=s;}
        require(selected!=null,"多工作表文件须包含名为“设备导入”的工作表");String relationship=selected.getAttribute("r:id"),path="";
        NodeList rels=xml(files.get("xl/_rels/workbook.xml.rels")).getElementsByTagName("Relationship");for(int i=0;i<rels.getLength();i++){Element r=(Element)rels.item(i);if(r.getAttribute("Id").equals(relationship)){String target=r.getAttribute("Target");path=target.startsWith("/")?target.substring(1):"xl/"+target;}}
        require(files.containsKey(path),"找不到工作表数据");List<String> shared=new ArrayList<>();if(files.containsKey("xl/sharedStrings.xml")){NodeList ss=xml(files.get("xl/sharedStrings.xml")).getElementsByTagName("si");for(int i=0;i<ss.getLength();i++)shared.add(ss.item(i).getTextContent());}
        Document doc=xml(files.get(path));require(doc.getElementsByTagName("f").getLength()==0,"文件包含公式，请先粘贴为纯值");require(doc.getElementsByTagName("mergeCell").getLength()==0,"首版不支持合并单元格");
        List<List<String>> rows=new ArrayList<>();NodeList rr=doc.getElementsByTagName("row");require(rr.getLength()<=10000,"工作表物理行数超过限制");
        for(int i=0;i<rr.getLength();i++){Element rowElement=(Element)rr.item(i);String rowNumber=rowElement.getAttribute("r");int physical=rowNumber.isEmpty()?i+1:Integer.parseInt(rowNumber);require(physical<=10000&&physical>rows.size(),"工作表行号无效或超过限制");while(rows.size()<physical-1)rows.add(new ArrayList<>());List<String> row=new ArrayList<>();NodeList cells=rowElement.getElementsByTagName("c");for(int j=0;j<cells.getLength();j++){
            Element c=(Element)cells.item(j);String ref=c.getAttribute("r");int col=0;for(char ch:ref.toCharArray()){if(ch<'A'||ch>'Z')break;col=col*26+ch-'A'+1;}require(col>=1&&col<=20,"工作表列数超过限制");while(row.size()<col)row.add("");
            String value="";NodeList v=c.getElementsByTagName("v");if(v.getLength()>0)value=v.item(0).getTextContent();String type=c.getAttribute("t");
            if(type.equals("s"))value=shared.get(Integer.parseInt(value));if(type.equals("inlineStr")){NodeList t=c.getElementsByTagName("t");StringBuilder b=new StringBuilder();for(int k=0;k<t.getLength();k++)b.append(t.item(k).getTextContent());value=b.toString();}
            require(value.length()<=10000,"单元格过长");row.set(col-1,value.trim());
        }rows.add(row);}return rows;
    }
}
