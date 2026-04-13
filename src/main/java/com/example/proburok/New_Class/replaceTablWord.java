package com.example.proburok.New_Class;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class replaceTablWord {

    public static void replacePlaceholderWithTableSimpleFixed(XWPFDocument doc, String placeholder, List<List<String>> tableData, int templateNumber,String idi) {
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            String text = paragraph.getText();

            if (text != null && text.contains(placeholder)) {
                // Эмулируем "Backspace" - удаляем placeholder из параграфа
                removePlaceholderFromParagraph(paragraph, placeholder);

                // Создаем таблицу в позиции, где был placeholder
                XmlCursor cursor = paragraph.getCTP().newCursor();
                XWPFTable table = doc.insertNewTbl(cursor);
                table.setWidth("100%");

                // Установить границы для таблицы
                table.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
                table.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
                table.setTopBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
                table.setBottomBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
                table.setLeftBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
                table.setRightBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");

                // Заполняем таблицу без лишних пробелов
                for (int rowIdx = 0; rowIdx < tableData.size(); rowIdx++) {
                    List<String> rowData = tableData.get(rowIdx);
                    XWPFTableRow row = (rowIdx == 0) ? table.getRow(0) : table.createRow();

                    // Убеждаемся, что в строке достаточно ячеек
                    while (row.getTableCells().size() < rowData.size()) {
                        row.addNewTableCell();
                    }

                    for (int colIdx = 0; colIdx < rowData.size(); colIdx++) {
                        XWPFTableCell cell = row.getCell(colIdx);
                        if (cell != null) {
                            // Полностью очищаем ячейку (как Backspace)
                            clearCellCompletely(cell);

                            // Создаем чистый параграф
                            XWPFParagraph para = cell.addParagraph();

                            // Убираем все отступы и интервалы
                            para.setAlignment(ParagraphAlignment.LEFT);
                            para.setIndentationLeft(0);
                            para.setIndentationRight(0);
                            para.setIndentationFirstLine(0);
                            para.setSpacingBetween(1);

                            // Создаем run и устанавливаем текст
                            XWPFRun run = para.createRun();
                            String cellText = rowData.get(colIdx);

                            // Убираем все пробелы по краям
                            if (cellText != null) {
                                cellText = cellText.trim();
                            }
                            run.setText(cellText == null ? "" : cellText);

                            // Убираем пробелы в самом run
                            run.setFontFamily("Times New Roman ");

                            run.setFontSize(11);

                            // Жирный для заголовка
                            if (rowIdx == 0) {
                                run.setBold(true);
                            }
                            if (templateNumber == 1 || templateNumber == 5 ) {
                                if (rowIdx == 6) {
                                    mergeCellsHorizontal(cell.getTableRow(), 0, 5, "", "expenses");
                                }

                            }else  if (templateNumber == 7 ){
                                    if (rowIdx == 5) {
                                        mergeCellsHorizontal(cell.getTableRow(),0,5,"","expenses");
                                    }
                            } else if (templateNumber == 2 ){
                              if (idi.equals("18")||idi.equals("20")){
                                  if (rowIdx == 1) {
                                      mergeCellsHorizontal(cell.getTableRow(),0,5,"При применении СПА в массиве:","expenses");
                                  }
                                  if (rowIdx == 7) {
                                      mergeCellsHorizontal(cell.getTableRow(),0,5,"При применении ФА в массиве:","expenses");
                                  }
                                  if (rowIdx == 11) {
                                      mergeCellsHorizontal(cell.getTableRow(),0,5,"При применении СПА в закладке:","expenses");
                                  }
                                  if (rowIdx == 17) {
                                      mergeCellsHorizontal(cell.getTableRow(),0,5,"При применении ФА в закладке:","expenses");
                                  }
                              }else {
                                  if (rowIdx == 1) {
                                      mergeCellsHorizontal(cell.getTableRow(), 0, 5, "При применении СПА:", "expenses");
                                  }
                                  if (rowIdx == 7) {
                                      mergeCellsHorizontal(cell.getTableRow(), 0, 5, "При применении ФА:", "expenses");
                                  }
                              }

                            } else if ( templateNumber == 3 ){
                                if (idi.equals("18")||idi.equals("20")){
                                    if (rowIdx == 1) {
                                        mergeCellsHorizontal(cell.getTableRow(),0,5,"При применении СПА в массиве:","expenses");
                                    }
                                    if (rowIdx == 7) {
                                        mergeCellsHorizontal(cell.getTableRow(),0,5,"При применении ФА в массиве:","expenses");
                                    }
                                    if (rowIdx == 11) {
                                        mergeCellsHorizontal(cell.getTableRow(),0,5,"","expenses");
                                    }
                                    if (rowIdx == 13) {
                                        mergeCellsHorizontal(cell.getTableRow(),0,5,"При применении СПА в закладке:","expenses");
                                    }
                                    if (rowIdx == 19) {
                                        mergeCellsHorizontal(cell.getTableRow(),0,5,"При применении ФА в закладке:","expenses");
                                    }
                                    if (rowIdx == 23) {
                                        mergeCellsHorizontal(cell.getTableRow(),0,5,"","expenses");
                                    }
                                }else {
                                    if (rowIdx == 1) {
                                        mergeCellsHorizontal(cell.getTableRow(), 0, 5, "При применении СПА:", "expenses");
                                    }
                                    if (rowIdx == 7) {
                                        mergeCellsHorizontal(cell.getTableRow(), 0, 5, "При применении ФА:", "expenses");
                                    }if (rowIdx == 11) {
                                        mergeCellsHorizontal(cell.getTableRow(),0,5,"","expenses");
                                    }
                                }

                            }else if ( templateNumber == 8 ){

                                    if (rowIdx == 1) {
                                        mergeCellsHorizontal(cell.getTableRow(),0,5,"Крепь усиления сопряжения","expenses");
                                    }
                                    if (rowIdx == 6) {
                                        mergeCellsHorizontal(cell.getTableRow(),0,5,"Крепь ниши разворота:","expenses");
                                    }
                                    if (rowIdx == 7) {
                                        mergeCellsHorizontal(cell.getTableRow(),0,5,"При применении СПА","expenses");
                                    }
                                    if (rowIdx == 13) {
                                        mergeCellsHorizontal(cell.getTableRow(),0,5,"При применении ФА","expenses");
                                    }
                                    if (rowIdx == 17) {
                                        mergeCellsHorizontal(cell.getTableRow(),0,5,"","expenses");
                                    }



                            }
                        }
                    }
                }
                cursor.dispose();
                if (paragraph.getText() != null && paragraph.getText().trim().isEmpty()) {
                    int pos = doc.getPosOfParagraph(paragraph);
                    if (pos >= 0) {
                        doc.removeBodyElement(pos);
                    }
                }
                break;
            }
        }
    }
    public static void replacePlaceholderWithTableGrafic(XWPFDocument doc, String placeholder, List<List<String>> tableData, int templateNumber) {
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            String text = paragraph.getText();

            if (text != null && text.contains(placeholder)) {
                // Эмулируем "Backspace" - удаляем placeholder из параграфа
                removePlaceholderFromParagraph(paragraph, placeholder);

                // Создаем таблицу в позиции, где был placeholder
                XmlCursor cursor = paragraph.getCTP().newCursor();
                XWPFTable table = doc.insertNewTbl(cursor);
                table.setWidth("101%");

                // Установить границы для таблицы
                table.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
                table.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
                table.setTopBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
                table.setBottomBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
                table.setLeftBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
                table.setRightBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
                table.setTableAlignment(TableRowAlign.CENTER);
                // Заполняем таблицу без лишних пробелов
                for (int rowIdx = 0; rowIdx < tableData.size(); rowIdx++) {
                    List<String> rowData = tableData.get(rowIdx);
                    XWPFTableRow row = (rowIdx == 0) ? table.getRow(0) : table.createRow();

                    // Убеждаемся, что в строке достаточно ячеек
                    while (row.getTableCells().size() < rowData.size()) {
                        row.addNewTableCell();
                    }

                    for (int colIdx = 0; colIdx < rowData.size(); colIdx++) {
                        XWPFTableCell cell = row.getCell(colIdx);
                        if (cell != null) {
                            // Полностью очищаем ячейку (как Backspace)
                            clearCellCompletely(cell);
                            // Создаем чистый параграф
                            XWPFParagraph para = cell.addParagraph();
                            // Убираем все отступы и интервалы
                            para.setAlignment(ParagraphAlignment.LEFT);
                            para.setIndentationLeft(0);
                            para.setIndentationRight(0);
                            para.setIndentationFirstLine(0);
                            para.setSpacingBetween(1);

                            // Создаем run и устанавливаем текст
                            XWPFRun run = para.createRun();
                            String cellText = (rowData.get(colIdx) != null) ? rowData.get(colIdx).trim() : "";
                            if ("Y".equals(cellText)) {
                                setCellBackgroundColor(cell, "C0C0C0"); //  "D3D3D3", "C0C0C0",E0E0E0
                                cellText = ""; // удаляем текст
                            } else if ("N".equals(cellText)) {
                                setCellBackgroundColor(cell, "FFFFFF"); // серый
                                cellText = "";

                        } else if ("O".equals(cellText)) {
                            setCellBackgroundColor(cell, "E0E0E0"); // серый
                            cellText = "";
                            }else {
                                setCellBackgroundColor(cell, "FFFFFF");
                            }

                            run.setText(cellText);
                            // Убираем пробелы в самом run
                            run.setFontFamily("Times New Roman ");
                            run.setFontSize(9);
                        }
                    }
                }
                mergeCellsHorizontal(table.getRow(0), 2, 24, "Время, ч", "grafic");
                mergeCellsHorizontal(table.getRow(1), 2, 8, "Смена I", "grafic");
                mergeCellsHorizontal(table.getRow(1), 8, 24, "Смена II", "grafic");
                mergeCellsVertical(table, 0, 2, 0, "№", "grafic");
                mergeCellsVertical(table, 0, 2, 1, "Наименование процесса", "grafic");
                cursor.dispose();
                if (paragraph.getText() != null && paragraph.getText().trim().isEmpty()) {
                    int pos = doc.getPosOfParagraph(paragraph);
                    if (pos >= 0) {
                        doc.removeBodyElement(pos);
                    }
                }
                break;
            }
        }

    }
    private static void setCellBackgroundColor(XWPFTableCell cell, String hexColor) {
        CTTc cttc = cell.getCTTc();
        CTTcPr tcPr = cttc.isSetTcPr() ? cttc.getTcPr() : cttc.addNewTcPr();
        CTShd shd = tcPr.isSetShd() ? tcPr.getShd() : tcPr.addNewShd();
        shd.setFill(hexColor);
    }
    private static void clearCellCompletely(XWPFTableCell cell) {
        // Удаляем все параграфы
        for (int i = cell.getParagraphs().size() - 1; i >= 0; i--) {
            cell.removeParagraph(i);
        }
        cell.getCTTc().setTcPr(null);
    }
    private static void removePlaceholderFromParagraph(XWPFParagraph paragraph, String placeholder) {
        String text = paragraph.getText();
        if (text != null && text.contains(placeholder)) {
            // Получаем все runs в параграфе
            List<XWPFRun> runs = paragraph.getRuns();

            // Собираем текст из всех runs
            StringBuilder fullText = new StringBuilder();
            for (XWPFRun run : runs) {
                if (run.getText(0) != null) {
                    fullText.append(run.getText(0));
                }
            }
            // Если placeholder найден в собранном тексте
            int placeholderIndex = fullText.indexOf(placeholder);
            if (placeholderIndex >= 0) {
                // Удаляем все runs
                for (int i = runs.size() - 1; i >= 0; i--) {
                    paragraph.removeRun(i);
                }
                // Создаем новый run с оставшимся текстом (без placeholder)
                String remainingText = fullText.toString().replace(placeholder, "");
                if (!remainingText.trim().isEmpty()) {
                    XWPFRun newRun = paragraph.createRun();
                    newRun.setText(remainingText);
                }
            }
        }
    }

    private static void mergeCellsHorizontal(XWPFTableRow row, int startCol, int endCol, String mergedText,String What) {
        if (startCol < 0 || endCol >= row.getTableCells().size() || startCol > endCol) {
            return;
        }

        // Очищаем все ячейки в диапазоне
        for (int i = startCol; i <= endCol; i++) {
            XWPFTableCell cell = row.getCell(i);
            clearCellCompletely(cell);
        }

        // Настраиваем объединение для первой ячейки
        XWPFTableCell firstCell = row.getCell(startCol);
        setCellMerge(firstCell, true, startCol == endCol ? 0 : endCol - startCol);

        // Заполняем текст в объединенной ячейке
        XWPFParagraph para = firstCell.addParagraph();
        para.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = para.createRun();
        run.setText(mergedText == null ? "" : mergedText.trim());
        if (What.equals("grafic")){
            run.setFontFamily("Times New Roman ");
            run.setFontSize(9);

        }else { run.setItalic(true);}

        // Настраиваем остальные ячейки как объединенные
        for (int i = startCol + 1; i <= endCol; i++) {
            XWPFTableCell cell = row.getCell(i);
            setCellMerge(cell, false, 0);
        }
    }

    // Метод для установки объединения ячейки
    private static void setCellMerge(XWPFTableCell cell, boolean isFirst, int span) {
        CTTc cttc = cell.getCTTc();
        CTTcPr tcPr = cttc.isSetTcPr() ? cttc.getTcPr() : cttc.addNewTcPr();

        if (isFirst && span > 0) {
            // Устанавливаем горизонтальный спан (объединение)
            CTHMerge hMerge = tcPr.addNewHMerge();
            hMerge.setVal(STMerge.RESTART);
            // Также можно установить gridSpan
            CTDecimalNumber gridSpan = CTDecimalNumber.Factory.newInstance();
            gridSpan.setVal(BigInteger.valueOf(span + 1));
            tcPr.setGridSpan(gridSpan);
        } else if (!isFirst) {
            // Продолжение объединения
            CTHMerge hMerge = tcPr.addNewHMerge();
            hMerge.setVal(STMerge.CONTINUE);
        }
    }
    private static void mergeCellsVertical(XWPFTable table, int startRow, int endRow, int col, String mergedText, String What) {
        if (startRow < 0 || endRow >= table.getNumberOfRows() || startRow > endRow) {
            return;
        }

        // Очищаем все ячейки в диапазоне
        for (int rowIdx = startRow; rowIdx <= endRow; rowIdx++) {
            XWPFTableRow row = table.getRow(rowIdx);
            XWPFTableCell cell = row.getCell(col);
            if (cell != null) {
                clearCellCompletely(cell);
            }
        }

        // Настраиваем объединение для верхней ячейки (restart)
        XWPFTableRow topRow = table.getRow(startRow);
        XWPFTableCell topCell = topRow.getCell(col);
        if (topCell == null) return;

        setCellMergeVertical(topCell, true);  // restart

        // Заполняем текст в верхней ячейке
        XWPFParagraph para = topCell.addParagraph();
        para.setAlignment(ParagraphAlignment.LEFT);
        para.setIndentationLeft(0);
        para.setIndentationRight(0);
        para.setIndentationFirstLine(0);
        para.setSpacingBetween(1);
        para.setWordWrap(true); // разрешить перенос

        XWPFRun run = para.createRun();
        String text = mergedText == null ? "" : mergedText.trim();
// Удаляем любые невидимые пробелы (например, &nbsp;)
        text = text.replaceAll("[\\s\\u00A0]+", " ").trim();
        run.setText(text);
        run.setFontFamily("Times New Roman");
        run.setFontSize(9);

        // Настраиваем остальные ячейки как продолжение объединения (continue)
        for (int rowIdx = startRow + 1; rowIdx <= endRow; rowIdx++) {
            XWPFTableRow row = table.getRow(rowIdx);
            XWPFTableCell cell = row.getCell(col);
            if (cell != null) {
                setCellMergeVertical(cell, false);
            }
        }
    }

    private static void setCellMergeVertical(XWPFTableCell cell, boolean isFirst) {
        CTTc cttc = cell.getCTTc();
        CTTcPr tcPr = cttc.isSetTcPr() ? cttc.getTcPr() : cttc.addNewTcPr();

        CTVMerge vMerge = tcPr.isSetVMerge() ? tcPr.getVMerge() : tcPr.addNewVMerge();
        if (isFirst) {
            vMerge.setVal(STMerge.RESTART);
        } else {
            vMerge.setVal(STMerge.CONTINUE);
        }
    }
}
