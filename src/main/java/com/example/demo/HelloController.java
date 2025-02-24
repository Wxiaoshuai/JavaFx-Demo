package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

public class HelloController {
    @FXML
    private Label fileName1;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private TextField signature1;
    @FXML
    private TextField signature2;
    @FXML
    private Button selectButton;
    @FXML
    private Button createButton1;
    @FXML
    private Button createButton2;

    @FXML
    private TableView<FileInfoEntity> tableView1;
    @FXML
    private TableColumn<FileInfoEntity, Boolean> selectColumn; // 复选框列
    @FXML
    private TableColumn<FileInfoEntity, String> columnCode;
    @FXML
    private TableColumn<FileInfoEntity, String> columnCategory;
    @FXML
    private TableColumn<FileInfoEntity, String> columnName;
    @FXML
    private TableColumn<FileInfoEntity, String> columnOwner;
    @FXML
    private TableColumn<FileInfoEntity, String> columnConclusion1;
    @FXML
    private TableColumn<FileInfoEntity, String> columnConclusion4;

    private static final String LAST_FILE_PATH_KEY = "lastPath";

    @FXML
    protected void onChooseFile() throws IOException {
        Preferences preferences = Preferences.userNodeForPackage(HelloController.class);
        String lastPath = preferences.get(LAST_FILE_PATH_KEY, System.getProperty("user.dir"));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(new File(lastPath));
        File file1 = fileChooser.showOpenDialog(null);
        if (file1 != null) {
            ObservableList<FileInfoEntity> fileInfoEntityList = FXCollections.observableArrayList();

            try {
                String filePath = file1.getName();

                // 读取 Excel 文件
                FileInputStream file = new FileInputStream(file1);

                // 根据文件类型创建 Workbook 对象
                Workbook workbook;
                if (filePath.endsWith(".xlsx")) {
                    workbook = new XSSFWorkbook(file); // .xlsx 文件
                } else if (filePath.endsWith(".xls")) {
                    workbook = new HSSFWorkbook(file); // .xls 文件
                } else {
                    fileName1.setText("不支持的文件格式!");
                    throw new IllegalArgumentException("不支持的文件格式");
                }
                fileName1.setText(file1.getPath());

                // 获取第一个工作表
                Sheet sheet = workbook.getSheetAt(0);

                // 遍历每一行
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    if (isRowEmpty(row)) {
                        break;
                    }
                    FileInfoEntity fileInfo = new FileInfoEntity();
                    // 遍历每一列
                    for (Cell cell : row) {
                        int columnIndex = cell.getColumnIndex();
                        // 根据单元格类型获取值
                        switch (cell.getCellType()) {
                            case STRING:
                                fileInfo.setFileInfo(columnIndex, cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    Date date = cell.getDateCellValue();
                                    String formatDate = formatDate(date);
                                    fileInfo.setFileInfo(columnIndex, formatDate);
                                } else {
                                    double numericCellValue = cell.getNumericCellValue();
                                    if (columnIndex > 5) {
                                        String formatDate = formatDate(DateUtil.getJavaDate(numericCellValue));
                                        fileInfo.setFileInfo(columnIndex, formatDate);
                                    } else {
                                        fileInfo.setFileInfo(columnIndex, String.valueOf(cell.getNumericCellValue()));
                                    }
                                }
                                break;
                            case BOOLEAN:
                                fileInfo.setFileInfo(columnIndex, String.valueOf(cell.getBooleanCellValue()));
                                break;
                            case FORMULA:
                                fileInfo.setFileInfo(columnIndex, cell.getCellFormula());
                                break;
                            default:
                                break;
                        }
                    }
                    fileInfoEntityList.add(fileInfo);
                }

                // 关闭文件流
                workbook.close();
                file.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fileInfoEntityList.size() > 0) {
                table(fileInfoEntityList);
            }
        } else {
            fileName1.setText("文件不存在，请重新选择！");
        }
    }
    @FXML
    protected void onHelloButtonClick() throws IOException {
        Preferences preferences = Preferences.userNodeForPackage(HelloController.class);
        String lastPath = preferences.get(LAST_FILE_PATH_KEY, System.getProperty("user.dir"));

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(lastPath));

        File file = directoryChooser.showDialog(null);
        if (file != null) {
            label1.setText(file.getPath());
        } else {
            label1.setText("错误，请重新选择！");
        }
    }

    @FXML
    protected void onHelloButtonClick3() throws IOException {
        if (label1.getText().equals("请先选择文件夹...") || label1.getText().equals("错误，请重新选择！")) {
            label2.setText("请先选择文件夹...");
            return;
        }
        if (StringUtil.isBlank(signature1.getText()) || StringUtil.isBlank(signature2.getText())) {
            label2.setText("请先输入落款信息...");
            return;
        }

        label2.setText("正在生成...");
        changeButton(true);

        // 获取选中的行
        boolean success = false;
        for (FileInfoEntity fileInfo : tableView1.getItems()) {
            if (fileInfo.isSelected()) {
                createWordFile(fileInfo);
                success = true;
            }
        }

        changeButton(false);
        if (success) {
            label2.setText("生成成功！");
        } else {
            label2.setText("生成失败！");
        }
    }

    private void changeButton(Boolean b) {
        selectButton.setDisable(b);
        createButton1.setDisable(b);
        createButton2.setDisable(b);
    }

    private void createWordFile(FileInfoEntity fileInfo) {
        String absolutePath = new File("").getAbsolutePath();
        // 模板文件路径
        String templatePath = absolutePath + "/src/main/resources/file/备案审查结论-模板.docx";
        // 输出文件路径
        String outputPath = label1.getText() + "/" + fileInfo.getCode() + ".docx";

        try {
            fileInfo.setConclusion2(signature1.getText());
            fileInfo.setConclusion3(signature2.getText());

            // 读取模板文件
            FileInputStream fis = new FileInputStream(templatePath);
            XWPFDocument document = new XWPFDocument(fis);

            // 替换模板中的占位符
//            replacePlaceholders(document, fileInfo);

            // 替换表格中的占位符
            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            replaceTextInParagraph(paragraph, fileInfo);
                        }
                    }
                }
            }

            // 保存生成的 Word 文件
            FileOutputStream fos = new FileOutputStream(outputPath);
            document.write(fos);
            fos.close();
            document.close();
            fis.close();
        } catch (Exception e) {
            label2.setText("生成失败！");
            e.printStackTrace();
        }
    }
    /**
     * 替换 Word 文档中的占位符
     */
    private static void replacePlaceholders(XWPFDocument document, FileInfoEntity fileInfo) throws IllegalAccessException {
        // 遍历文档中的段落
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceTextInParagraph(paragraph, fileInfo);
        }
    }

    /**
     * 替换段落中的占位符
     *
     */
    private static void replaceTextInParagraph(XWPFParagraph paragraph, FileInfoEntity fileInfo) throws IllegalAccessException {
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs != null) {
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                if (text != null) {
                    if (text.contains("conclusion2") || text.contains("conclusion3") || text.contains("conclusion4")) {
                        run.setBold(true);
                    }
                    // 替换占位符
                    for (Field declaredField : fileInfo.getClass().getDeclaredFields()) {
                        declaredField.setAccessible(true);
                        Object object = declaredField.get(fileInfo);
                        if (object != null) {
                            text = text.replace("{" + declaredField.getName() + "}", declaredField.get(fileInfo).toString());
                        }
                    }
                    run.setText(text, 0);
                }
            }
        }
    }

    /**
     * 判断一行是否为空
     *
     * @param row 行对象
     * @return 如果行为空返回 true，否则返回 false
     */
    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        // 遍历行中的每个单元格
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false; // 如果单元格不为空，返回 false
            }
        }
        return true; // 所有单元格都为空，返回 true
    }

    /**
     * 将日期格式化为字符串
     *
     * @param date 日期对象
     * @return 格式化后的日期字符串
     */
    private static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = dateFormat.format(date);
        return format.replaceFirst("-", "年").replaceFirst("-", "月") + "日";
    }

    private void table(ObservableList<FileInfoEntity> fileInfoEntityList) {
        // 创建 TableView

        // 将列添加到 TableView
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
//        selectColumn.setCellValueFactory(cellData -> cellData.getValue().getCheckBox());

//        selectColumn.setCellValueFactory(new SimpleStringProperty<>("selected"));
        columnCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnOwner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        columnConclusion1.setCellValueFactory(new PropertyValueFactory<>("conclusion1"));
        columnConclusion4.setCellValueFactory(new PropertyValueFactory<>("conclusion4"));

        // 设置复选框列的单元格工厂
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        // 将数据添加到 TableView
        tableView1.setItems(fileInfoEntityList);
        tableView1.setEditable(true);


        // 监听复选框状态变化
//        selectColumn.setCellFactory(column -> {
//            CheckBoxTableCell<FileInfoEntity, Boolean> cell = new CheckBoxTableCell<>();
//            cell.selectedStateCallbackProperty().addListener((obs, wasSelected, isNowSelected) -> {
//                FileInfoEntity fileInfo = tableView1.getItems().get(cell.getIndex());
//                System.out.println("复选框状态变化: " + fileInfo.getCode() + " - " + isNowSelected);
//            });
//            return cell;
//        });
    }

}