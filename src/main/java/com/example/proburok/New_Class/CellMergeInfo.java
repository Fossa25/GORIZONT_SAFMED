package com.example.proburok.New_Class;

class CellMergeInfo {
    private int row;           // Номер строки (0-based)
    private int startCol;      // Начальный столбец для объединения
    private int endCol;        // Конечный столбец для объединения
    private String text;       // Текст для объединенной ячейки

    public CellMergeInfo(int row, int startCol, int endCol, String text) {
        this.row = row;
        this.startCol = startCol;
        this.endCol = endCol;
        this.text = text;
    }

    // Getters
    public int getRow() { return row; }
    public int getStartCol() { return startCol; }
    public int getEndCol() { return endCol; }
    public String getText() { return text; }
}
