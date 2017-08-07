package de.clearit.test.framework.elemente;

import java.util.Arrays;
import java.util.List;

import de.clearit.test.exceptions.TableRowNotFoundException;

public class WebTableUtil
{

   /**
    * 
    * @param rowsAndColumns
    * @param gesuchteSpalten
    * @return den index der zeile beginnend bei 0, sonst -1
    */
   public static int findeZeile(List<WebTableRowWithColumns> rowsAndColumns, String... gesuchteSpalten)
   {
      return findeZeile(0, rowsAndColumns, gesuchteSpalten);
   }

   /**
    * 
    * @param startSpalteBeiDerTabelle
    *           beginnend bei 0
    * @param rowsAndColumns
    * @param gesuchteSpalten
    * @return den index der zeile beginnend bei 0, sonst -1
    */
   private static int findeZeile(int startSpalteBeiDerTabelle, List<WebTableRowWithColumns> rowsAndColumns,
         String... gesuchteSpalten)
   {
      boolean gefunden = false;
      int zeile = -1;
      int zeileGefunden = -1;
      for (WebTableRowWithColumns row : rowsAndColumns)
      {
         zeile++;
         boolean alleSpaltenGefunden = true;
         for (int spalte = 0; spalte < gesuchteSpalten.length; spalte++)
         {
            if (spalteNichtGleich(gesuchteSpalten, row, spalte, startSpalteBeiDerTabelle))
            {
               alleSpaltenGefunden = false;
               break;
            }
         }
         gefunden = alleSpaltenGefunden;
         if (gefunden)
         {
            zeileGefunden = zeile;
            break;
         }
      }
      return zeileGefunden;
   }

   /**
    * 
    * @param rows
    * @param zeileContainsOhneLeerzeichen
    * @return den index der zeile beginnend bei 0, sonst -1
    */
   public static int findeZeile(List<String> rows, String zeileContainsOhneLeerzeichen)
   {
      int zeile = -1;
      int zeileGefunden = -1;
      for (String row : rows)
      {
         zeile++;
         if (row.replaceAll(" ", "").contains(zeileContainsOhneLeerzeichen.replaceAll(" ", "")))
         {
            zeileGefunden = zeile;
            break;
         }
      }
      return zeileGefunden;
   }

   /**
    * 
    * @param rows
    * @param zeileContains
    * @return true wenn eine Zeile den Text beinhaltet
    */
   public static boolean existiertZeile(List<String> rows, String zeileContainsOhneLeerzeichen)
   {
      return findeZeile(rows, zeileContainsOhneLeerzeichen) > -1;
   }

   /**
    * 
    * @param rowsAndColumns
    * @param gesuchteSpalten
    * @return true wenn alle spalten gefunden werden, sonst false
    */
   public static boolean existiertZeile(List<WebTableRowWithColumns> rowsAndColumns, String... gesuchteSpalten)
   {
      return findeZeile(rowsAndColumns, gesuchteSpalten) > -1;
   }

   private static boolean spalteNichtGleich(String[] gesuchteSpalten, WebTableRowWithColumns row, int spalte,
         int startSpalteBeiDerTabelle)
   {
      String spalteDieGesuchtIst = gesuchteSpalten[spalte];
      if (spalteDieGesuchtIst.equals("*"))
      {
         return false;
      }
      if (!row.hasColumns())
      {
         return true;
      }
      return !equalsIgnoreNewLine(row.getColumns().get(spalte + startSpalteBeiDerTabelle).trim(), spalteDieGesuchtIst);
   }

   private static boolean equalsIgnoreNewLine(String spalteInTabelle, String gesuchteSpalte)
   {
      String spalteInTabelleNoNewLine = toLineString(spalteInTabelle);
      String gesuchteSpalteNoNewline = toLineString(gesuchteSpalte);
      return spalteInTabelleNoNewLine.equals(gesuchteSpalteNoNewline);
   }

   public static String zeilenAlsString(List<WebTableRowWithColumns> rowsAndColumns)
   {
      return zeilenAlsString(rowsAndColumns, null);
   }

   public static String zeilenAlsString(List<WebTableRowWithColumns> rowsAndColumns, Integer maxAnzahlSpalten)
   {
      StringBuilder result = new StringBuilder();
      for (WebTableRowWithColumns webTableRow : rowsAndColumns)
      {
         result.append(zeileAlsString(webTableRow, maxAnzahlSpalten)).append("\n");
      }
      return result.toString();
   }

   public static String zeileAlsString(WebTableRowWithColumns webTableRow)
   {
      return toLineString(webTableRow.toString());
   }

   public static String zeileAlsString(WebTableRowWithColumns webTableRow, Integer maxAnzahlSpalten)
   {
      return toLineString(webTableRow.toString(webTableRow.getColumns().size()));
   }

   public static String zeileAlsString(String... gesuchteSpalten)
   {
      return toLineString(Arrays.asList(gesuchteSpalten).toString());
   }

   public static TableRowNotFoundException createTableRowNotFoundException(List<WebTableRowWithColumns> rowsAndColumns,
         String... gesuchteSpalten)
   {
      String text = createTableRowNotFoundText(rowsAndColumns, gesuchteSpalten);
      return new TableRowNotFoundException("der gesuchten " + text);
   }

   private static String createTableRowNotFoundText(List<WebTableRowWithColumns> rowsAndColumns,
         String... gesuchteSpalten)
   {
      String text = "Zeile nicht gefunden.\n\nGesuchte Spalten waren:\n" + WebTableUtil.zeileAlsString(gesuchteSpalten)
            + "\n\nZeilen in Tabelle waren:\n" + WebTableUtil.zeilenAlsString(rowsAndColumns, gesuchteSpalten.length)
            + "\n";
      return text;
   }

   public static String createTableRowFoundText(List<WebTableRowWithColumns> rowsAndColumns, String... gesuchteSpalten)
   {
      String text = "Zeile gefunden, wurde jedoch nicht erwartet.\n\nGesuchte Spalten waren:\n"
            + WebTableUtil.zeileAlsString(gesuchteSpalten) + "\n\nZeilen in Tabelle waren:\n"
            + WebTableUtil.zeilenAlsString(rowsAndColumns, gesuchteSpalten.length) + "\n";
      return text;
   }

   private static String toLineString(String row)
   {
      String result = row.replace("\n", "").replace("\r", "");
      result = result.replace("ui-button", "");
      return result;
   }

}
