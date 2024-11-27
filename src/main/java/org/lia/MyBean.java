package org.lia;

import jakarta.ejb.ApplicationException;
import jakarta.enterprise.context.ApplicationScoped;
import org.lia.entity.PointEntity;

import javax.faces.bean.ManagedBean;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ManagedBean
@ApplicationScoped
public class MyBean {
    private Map<Long, Boolean> selectedValues = new HashMap<>();
    private String y;
    private String svgY;
    private String r;
    private String x;
    ArrayList<PointEntity> res;
    DBManager dbManager;
    private String message;
    private final String messageIncorrectInput = "Некорректный ввод";

    public MyBean() {
        x = "";
        dbManager = new DBManager();
        try {
            res = dbManager.getPoints();
        } catch (SQLException ignore) {}
    }

    public String getSvgY() {
        return svgY;
    }

    public void setSvgY(String svgY) {
        this.svgY = svgY;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<PointEntity> getRes() {
        return res;
    }

    public void setRes(ArrayList<PointEntity> res) {
        this.res = res;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public Map<Long, Boolean> getSelectedValues() {
        return selectedValues;
    }

    public void setSelectedValues(Map<Long, Boolean> selectedValues) {
        this.selectedValues = selectedValues;
    }

    public void submit() {
        message = "";
        java.sql.Timestamp startDate = new java.sql.Timestamp(System.currentTimeMillis());
        Boolean resultCheck = true;
        Double selectedX = -100.0;
        double selectedY = -100.0;
        double selectedR = -100.0;
        if (!x.isEmpty()) {
            selectedX = Double.parseDouble(x);
            selectedY = Double.parseDouble(svgY);
            x = "";
            svgY = "";
        } else {
            for (Long key : selectedValues.keySet()) {
                if (selectedValues.get(key)) {
                    if (selectedX == -100.0) {
                        selectedX = (double) key;
                    } else {
                        resultCheck = false;
                        break;
                    }
                }
            }
        }

        try {
            if (selectedY == -100.0) {
                selectedY = Double.parseDouble(y);
            }
            selectedR = Double.parseDouble(r);
            if (selectedY < -5 || selectedY > 5) {
                resultCheck = false;
            }
        } catch (ClassCastException | NullPointerException | NumberFormatException e) {
            resultCheck = false;
        }
        if (resultCheck) {
            System.out.println(resultCheck + " " + selectedX + " " + selectedY + " " + selectedR);
            PointEntity point = new PointEntity();
            point.setX((double)selectedX);
            point.setY(selectedY);
            point.setR(selectedR);
            point.setDate(startDate);
            boolean result = selectedX >= 0 && selectedY >= 0 && (double) selectedX <= selectedR && selectedY <= selectedR ||
                    selectedX >= 0 && selectedY <= 0 && selectedX * selectedX + selectedY * selectedY <= selectedR * selectedR ||
                    selectedX <= 0 && selectedY >= 0 && selectedY <= (double) selectedX / 2 + selectedR / 2;
            point.setResult(result);

            try {
                dbManager.sendPoint(point);
                res = dbManager.getPoints();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            message = messageIncorrectInput;
        }
    }

    public String generateSvg() {
        StringBuilder svgBuilder = new StringBuilder();
        svgBuilder.append("<svg width=\"300px\" height=\"300px\" onclick=\"svgHandler(event)\" class=\"svgClass\" id=\"graphSvg\">\n");
        svgBuilder.append("<line x1=\"0\" x2=\"300\" y1=\"150\" y2=\"150\" stroke=\"#343548\"></line>\n" +
                "                <line x1=\"150\" x2=\"150\" y1=\"0\" y2=\"300\" stroke=\"#343548\"></line>\n" +
                "                <polygon points=\"150,0 145,10 155,10\" stroke=\"#343548\"></polygon>\n" +
                "                <polygon points=\"300,150 290,145 290,155\" stroke=\"#343548\"></polygon>\n" +
                "\n" +
                "                <path d=\"M250,150 A100,100 90 0,1 150,250 L 150,150 Z\" fill=\"#87CEEB\"></path>\n" +
                "                <polygon points=\"150,150 250,150 250,50 150,50\" fill=\"#87CEEB\"></polygon>\n" +
                "                <polygon points=\"50,150 150,100 150,150\" fill=\"#87CEEB\"></polygon>\n" +
                "\n" +
                "                <line x1=\"50\" x2=\"50\" y1=\"145\" y2=\"155\" stroke=\"#343548\"></line>\n" +
                "                <line x1=\"100\" x2=\"100\" y1=\"145\" y2=\"155\" stroke=\"#343548\"></line>\n" +
                "                <line x1=\"200\" x2=\"200\" y1=\"145\" y2=\"155\" stroke=\"#343548\"></line>\n" +
                "                <line x1=\"250\" x2=\"250\" y1=\"145\" y2=\"155\" stroke=\"#343548\"></line>\n" +
                "\n" +
                "                <line x1=\"145\" x2=\"155\" y1=\"50\" y2=\"50\" stroke=\"#343548\"></line>\n" +
                "                <line x1=\"145\" x2=\"155\" y1=\"100\" y2=\"100\" stroke=\"#343548\"></line>\n" +
                "                <line x1=\"145\" x2=\"155\" y1=\"200\" y2=\"200\" stroke=\"#343548\"></line>\n" +
                "                <line x1=\"145\" x2=\"155\" y1=\"250\" y2=\"250\" stroke=\"#343548\"></line>\n" +
                "\n" +
                "                <text x=\"290\" y=\"140\">X</text>\n" +
                "                <text x=\"155\" y=\"10\">Y</text>\n" +
                "                <text x=\"40\" y=\"138\">-R</text>\n" +
                "                <text x=\"85\" y=\"138\">-R/2</text>\n" +
                "                <text x=\"190\" y=\"138\">R/2</text>\n" +
                "                <text x=\"245\" y=\"138\">R</text>\n" +
                "                <text x=\"162\" y=\"54\">R</text>\n" +
                "                <text x=\"162\" y=\"104\">R/2</text>\n" +
                "                <text x=\"162\" y=\"204\">-R/2</text>\n" +
                "                <text x=\"162\" y=\"254\">-R</text>");

        for (PointEntity point : res) {
            svgBuilder.append("<circle class=\"shot\" cx=\"");
            svgBuilder.append(150 + 50 * 2/ point.getR() * point.getX());
            svgBuilder.append("\"\ncy=\"");
            svgBuilder.append(150 - 50 * 2/ point.getR() * point.getY());
            svgBuilder.append("\" r=\"2\"\nfill=\"");
            if (point.getResult()) {
                svgBuilder.append("green");
            } else {
                svgBuilder.append("red");
            }
            svgBuilder.append("\" stroke-width=\"0\"></circle>");
        }

        svgBuilder.append("</svg>");
        return svgBuilder.toString();
    }
}
