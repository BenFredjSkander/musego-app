package tn.musego.app.gui;


import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PieChart;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import tn.musego.app.entities.Evenement;
import tn.musego.app.services.ServiceEvenement;

import java.util.ArrayList;
import java.util.HashSet;

public class EvenementStatsForm extends Form {


    public EvenementStatsForm(Form previous) {
        super("Statistiques des événements");
        setSafeArea(true);
        createPieChartForm();
        if (previous != null) {
            this.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        }

    }

    private void createPieChartForm() {
        ArrayList<Evenement> evenements = ServiceEvenement.getInstance().getAllEvenements();

// Créez un HashSet pour stocker les éléments uniques
        HashSet<Evenement> ensembleEvenements = new HashSet<>(evenements);

// Effacez l'ArrayList existant
        evenements.clear();

// Ajoutez les éléments uniques à l'ArrayList
        evenements.addAll(ensembleEvenements);
        String[] eventNames = new String[evenements.size()];
        Integer[] values = new Integer[evenements.size()];

        for (int i = 0; i < evenements.size(); i++) {
            values[i] = evenements.get(i).getNb_participants();
            eventNames[i] = evenements.get(i).getNom();
        }

        // Set up the renderer
        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.GREEN, ColorUtil.BLACK};
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setChartTitleTextSize(60);
        renderer.setDisplayValues(true);
        renderer.setShowLabels(true);
        renderer.setLabelsColor(ColorUtil.BLACK);

        CategorySeries dataset = buildCategoryDataset(values, eventNames);

        // Create the pie chart
        PieChart chart = new PieChart(dataset, renderer);

        ChartComponent chartComponent = new ChartComponent(chart);
        add(chartComponent);

    }


    private DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(50);
        renderer.setLegendTextSize(50);
        renderer.setMargins(new int[]{20, 30, 15, 0});
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    private CategorySeries buildCategoryDataset(Integer[] values, String[] eventNames) {
        CategorySeries series = new CategorySeries("Nombre de participants");
        for (int i = 0; i < values.length; i++) {
            series.add(eventNames[i], values[i]);
        }
        return series;
    }


}







