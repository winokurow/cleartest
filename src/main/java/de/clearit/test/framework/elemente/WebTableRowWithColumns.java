package de.clearit.test.framework.elemente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebTableRowWithColumns
{

   private List<String> columns = new ArrayList<>();

   void insertColumn(String column)
   {
      columns.add(column);
   }

   public List<String> getColumns()
   {
      return Collections.unmodifiableList(columns);
   }

   public boolean hasColumns()
   {
      return columns.size() > 0;
   }

   @Override
   public String toString()
   {
      return columns.toString();
   }

   public String toString(Integer maxAnzahlSpalten)
   {
      List<String> columnsToShow = new ArrayList<>();
      for (int i = 0; i < maxAnzahlSpalten; i++)
      {
         if (i < columns.size())
         {
            columnsToShow.add(columns.get(i));
         }
      }
      return columnsToShow.toString();
   }
}
