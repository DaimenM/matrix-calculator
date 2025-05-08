package com.matrix.matrixcalculator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
@Controller
public class MainController {
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
    @PostMapping("/")
    @ResponseBody
    public Map<String, Object> getValues(@RequestBody Map<String, Object> values) {
        try{
        switch(values.get("operation").toString()) {
            case "add":
                return add(values);
            case "subtract":
                return subtract(values);
            case "multiply":
                return multiply(values);
            case "manipulate":
                return manipulateRows(values);
            case "RREF":
                return rref(values);
            case "inverse":
                return inverse(values);
            case "determinant":
                return determinant(values);
            case "exponent":
                return exponent(values);
            case "transpose":
                return transpose(values);
            default:
                return Map.of("error", "Invalid Operation chosen");
        }
        
        
}catch(Exception e){
    //e.printStackTrace();
    return Map.of("error", e);
}
    
    }
    public static Map<String, Object> add(Map<String,Object> values){
        String result;
        Matrix resultMatrix;
        String[] resultArray;
        Matrix matrix1 = new Matrix(Integer.parseInt(values.get("matrix1Rows").toString()),
                Integer.parseInt(values.get("matrix1Cols").toString()));
                matrix1.addEntries(values.get("matrix1Entries").toString());
                Matrix matrix2 = new Matrix(Integer.parseInt(values.get("matrix2Rows").toString()),
                Integer.parseInt(values.get("matrix2Cols").toString()));
                matrix2.addEntries(values.get("matrix2Entries").toString());
                resultMatrix = matrix1.add(matrix2);
                result = resultMatrix.toString();
                resultArray = result.split(",");
                return Map.of(
                    "entries", Arrays.asList(resultArray),
                    "rows", resultMatrix.getRows(),
                    "columns", resultMatrix.getColumns()
                );
    }
    public static Map<String, Object> subtract (Map<String,Object> values){
        Matrix matrix1 = new Matrix(Integer.parseInt(values.get("matrix1Rows").toString()),
                Integer.parseInt(values.get("matrix1Cols").toString()));
                matrix1.addEntries(values.get("matrix1Entries").toString());
                Matrix matrix2 = new Matrix(Integer.parseInt(values.get("matrix2Rows").toString()),
                Integer.parseInt(values.get("matrix2Cols").toString()));
                matrix2.addEntries(values.get("matrix2Entries").toString());
                Matrix resultMatrix = matrix1.subtract(matrix2);
                String[] resultArray = resultMatrix.toString().split(",");
                return Map.of(
                    "entries", Arrays.asList(resultArray),
                    "rows", resultMatrix.getRows(),
                    "columns", resultMatrix.getColumns()
                );
    }
    public static Map<String, Object> multiply (Map<String,Object> values){
        Matrix matrix1 = new Matrix(Integer.parseInt(values.get("matrix1Rows").toString()),
                Integer.parseInt(values.get("matrix1Cols").toString()));
                matrix1.addEntries(values.get("matrix1Entries").toString());
                Matrix matrix2 = new Matrix(Integer.parseInt(values.get("matrix2Rows").toString()),
                Integer.parseInt(values.get("matrix2Cols").toString()));
                matrix2.addEntries(values.get("matrix2Entries").toString());
                Matrix resultMatrix = matrix1.multiply(matrix2);
                String[] resultArray = resultMatrix.toString().split(",");
                return Map.of(
                    "entries", Arrays.asList(resultArray),
                    "rows", resultMatrix.getRows(),
                    "columns", resultMatrix.getColumns()
                );
    }
    public static Map<String,Object> manipulateRows(Map<String,Object> values){
        Matrix matrix = new Matrix(Integer.parseInt(values.get("matrixRows").toString()),
            Integer.parseInt(values.get("matrixCols").toString()));
            matrix.addEntries(values.get("matrixEntries").toString());
        if(Integer.parseInt(values.get("mode").toString())==1){
            matrix.manipulateRow(Integer.parseInt(values.get("matrixRow").toString())-1,values.get("multiplier").toString());
            String[] resultArray = matrix.toString().split(",");
            return Map.of(
                "entries", Arrays.asList(resultArray),
                "rows", matrix.getRows(),
                "columns", matrix.getColumns()
            );
        }
        else if(Integer.parseInt(values.get("mode").toString())==2){
            if(values.get("rowOperation").toString().equals("switch")) matrix.switchRows(Integer.parseInt(values.get("row1").toString())-1, 
            Integer.parseInt(values.get("row2").toString())-1);
            matrix.manipulateRow(Integer.parseInt(values.get("row1").toString())-1, 
            Integer.parseInt(values.get("row2").toString())-1,
            values.get("multiplier").toString() ,
            values.get("rowOperation").toString());
            String[] resultArray = matrix.toString().split(",");
            return Map.of(
                "entries", Arrays.asList(resultArray),
                "rows", matrix.getRows(),
                "columns", matrix.getColumns()
            );
        }
        else return Map.of("error", "Invalid mode for matrix manipulation");
    }
    public static Map<String,Object> rref(Map<String, Object> values){
        String result;
        Matrix resultMatrix;
        String[] resultArray;
        Matrix matrix = new Matrix(Integer.parseInt(values.get("matrixRows").toString()),
                Integer.parseInt(values.get("matrixCols").toString()));
                matrix.addEntries(values.get("matrixEntries").toString());
                resultMatrix = matrix.RREF();
                result = resultMatrix.toString();
                resultArray = result.split(",");
                return Map.of(
                    "entries", Arrays.asList(resultArray),
                    "rows", resultMatrix.getRows(),
                    "columns", resultMatrix.getColumns()
                );
    }
    public static Map<String, Object> inverse(Map<String,Object> values){
        Matrix matrix  = new Matrix(Integer.parseInt(values.get("matrixRows").toString()),
                Integer.parseInt(values.get("matrixCols").toString()));
                matrix.addEntries(values.get("matrixEntries").toString());
                Matrix resultMatrix = matrix.inverse();
                String[] resultArray = resultMatrix.toString().split(",");
                return Map.of(
                    "entries", Arrays.asList(resultArray),
                    "rows", resultMatrix.getRows(),
                    "columns",resultMatrix.getColumns()
                );
    }
    public static Map<String,Object> determinant(Map<String,Object> values){
        Matrix matrix = new Matrix(Integer.parseInt(values.get("matrixRows").toString()),
                Integer.parseInt(values.get("matrixCols").toString()));
                matrix.addEntries(values.get("matrixEntries").toString());
                Fraction result = matrix.getDeterminant(matrix);
                return Map.of(
                    "entries", Arrays.asList(result.toString()),
                    "rows", 1,
                    "columns",1
                );}
    public static Map<String,Object> exponent(Map<String,Object> values){
        Matrix matrix = new Matrix(Integer.parseInt(values.get("matrixRows").toString()),
        Integer.parseInt(values.get("matrixCols").toString()));
        matrix.addEntries(values.get("matrixEntries").toString());
        int exponent = Integer.parseInt(values.get("exponent").toString());
        Matrix resulMatrix = matrix.exponent(exponent);
        String[] resultArray = resulMatrix.toString().split(",");
        return Map.of(
            "entries", Arrays.asList(resultArray),
            "rows", resulMatrix.getRows(),
            "columns", resulMatrix.getColumns()
        );

    }
    public static Map<String, Object> transpose(Map<String,Object> values){
        Matrix matrix = new Matrix(Integer.parseInt(values.get("matrixRows").toString()),
        Integer.parseInt(values.get("matrixCols").toString()));
        matrix .addEntries(values.get("matrixEntries").toString());
        Matrix resultMatrix = matrix.transpose();
        String[] resultArray = resultMatrix.toString().split(",");
        return Map.of(
            "entries",Arrays.asList(resultArray),
            "rows",resultMatrix.getRows(),
            "columns", resultMatrix.getColumns()
        );
    }
}