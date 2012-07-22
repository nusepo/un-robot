package imu.view;

import java.util.HashMap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author David Saldana
 */
public class GraficoSerieXY extends ChartPanel {

    private String titulo = "Titulo";
    private String tituloEjeX = "Tiempo";
    private String tituloEjeY = "Valor";
    private JFreeChart graficoSerieTiempo;
    private TimeSeriesCollection dataset;
    private HashMap<String, TimeSeries> series;

    public GraficoSerieXY() {
        //llamar al constructor de la clase superior pero sin un chart
        super(null);

        series = new HashMap<String, TimeSeries>();

        //crear un conjunto de datos
//        DefaultKeyedValuesDataset conjuntoDatos = new DefaultKeyedValuesDataset();
//        conjuntoDatos.setValue("Envigado", 150);
//        conjuntoDatos.setValue("Medell�n", 160);
//        conjuntoDatos.setValue("El retiro", 6);
//        conjuntoDatos.setValue("La ceja", 40);

        dataset = new TimeSeriesCollection();


        //crear un gráfico de XY
        graficoSerieTiempo = ChartFactory.createXYLineChart("Titulo", "Tiempo", "Valor", dataset, PlotOrientation.HORIZONTAL, true, true, true);

        graficoSerieTiempo = ChartFactory.createTimeSeriesChart(
                "Titulo", // title
                "Tiempo", // x-axis label              
                "Valor", // y-axis label        
                dataset, // data
                true, // create legend?
                true, // generate tooltips?
                false // generate URLs?
                );


        setChart(graficoSerieTiempo);
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        graficoSerieTiempo.setTitle(titulo);
        this.titulo = titulo;
    }

    /**
     * @return the tituloEjeX
     */
    public String getTituloEjeX() {
        return tituloEjeX;
    }

    /**
     * @param tituloEjeX the tituloEjeX to set
     */
    public void setTituloEjeX(String tituloEjeX) {
        this.tituloEjeX = tituloEjeX;
    }

    /**
     * @return the tituloEjeY
     */
    public String getTituloEjeY() {
        return tituloEjeY;
    }

    /**
     * @param tituloEjeY the tituloEjeY to set
     */
    public void setTituloEjeY(String tituloEjeY) {
        this.tituloEjeY = tituloEjeY;
    }

    public synchronized void agregarValor(final String nombreSerie, final double valor) {

        TimeSeries serie = series.get(nombreSerie);
        if (serie == null) {
            serie = new TimeSeries(nombreSerie, Millisecond.class);
            serie.setMaximumItemCount(1000);
            series.put(nombreSerie, serie);
            dataset.addSeries(serie);
        }

        serie.addOrUpdate(new Millisecond(), valor);
    }
}
