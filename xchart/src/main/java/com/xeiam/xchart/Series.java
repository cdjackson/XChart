/**
 * Copyright 2011 - 2013 Xeiam LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xeiam.xchart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
import com.xeiam.xchart.internal.markers.Marker;
import com.xeiam.xchart.internal.style.SeriesColorMarkerLineStyle;

/**
 * A Series containing X and Y data to be plotted on a Chart
 * 
 * @author timmolter
 */
public class Series {

  private String name = "";

  private Collection<?> xData;
  private AxisType xAxisType;

  private Collection<Number> yData;
  private AxisType yAxisType;

  private Collection<Number> errorBars;

  /** the minimum value of axis range */
  private BigDecimal xMin;

  /** the maximum value of axis range */
  private BigDecimal xMax;

  /** the minimum value of axis range */
  private BigDecimal yMin;

  /** the maximum value of axis range */
  private BigDecimal yMax;

  /** Line Style */
  private BasicStroke stroke;

  /** Line Color */
  private Color strokeColor;

  /** Marker Style */
  private Marker marker;

  /** Marker Color */
  private Color markerColor;

  /**
   * Constructor
   * 
   * @param name
   * @param xData
   * @param xAxisType
   * @param yData
   * @param yAxisType
   * @param errorBars
   * @param seriesColorMarkerLineStyle
   */
  public Series(String name, Collection<?> xData, AxisType xAxisType, Collection<Number> yData, AxisType yAxisType, Collection<Number> errorBars, SeriesColorMarkerLineStyle seriesColorMarkerLineStyle) {

    if (name == null || name.length() < 1) {
      throw new IllegalArgumentException("Series name cannot be null or zero-length!!!");
    }
    this.name = name;
    this.xData = xData;
    this.xAxisType = xAxisType;
    this.yData = yData;
    this.yAxisType = yAxisType;
    this.errorBars = errorBars;

    strokeColor = seriesColorMarkerLineStyle.getColor();
    markerColor = seriesColorMarkerLineStyle.getColor();
    marker = seriesColorMarkerLineStyle.getMarker();
    stroke = seriesColorMarkerLineStyle.getStroke();

    calculateMinMax();

  }

  /**
   * Finds the min and max of a dataset
   * 
   * @param data
   * @return
   */
  private BigDecimal[] findMinMax(Collection<?> data, AxisType axisType) {

    BigDecimal min = null;
    BigDecimal max = null;

    for (Object dataPoint : data) {

      if (dataPoint == null) {
        continue;
      }

      BigDecimal bigDecimal = null;

      if (axisType == AxisType.Number) {
        bigDecimal = new BigDecimal(((Number) dataPoint).toString());

      }
      else if (axisType == AxisType.Date) {
        Date date = (Date) dataPoint;
        bigDecimal = new BigDecimal(date.getTime());
      }
      else if (axisType == AxisType.String) {
        return new BigDecimal[] { null, null };
      }
      if (min == null || bigDecimal.compareTo(min) < 0) {
        min = bigDecimal;
      }
      if (max == null || bigDecimal.compareTo(max) > 0) {
        max = bigDecimal;
      }
    }

    return new BigDecimal[] { min, max };
  }

  /**
   * Finds the min and max of a dataset accounting for error bars
   * 
   * @param data
   * @return
   */
  private BigDecimal[] findMinMaxWithErrorBars(Collection<Number> data, Collection<Number> errorBars) {

    BigDecimal min = null;
    BigDecimal max = null;

    Iterator<Number> itr = data.iterator();
    Iterator<Number> ebItr = errorBars.iterator();
    while (itr.hasNext()) {
      BigDecimal bigDecimal = new BigDecimal(itr.next().doubleValue());
      BigDecimal eb = new BigDecimal(ebItr.next().doubleValue());
      if (min == null || (bigDecimal.subtract(eb)).compareTo(min) < 0) {
        min = bigDecimal.subtract(eb);
      }
      if (max == null || (bigDecimal.add(eb)).compareTo(max) > 0) {
        max = bigDecimal.add(eb);
      }
    }
    return new BigDecimal[] { min, max };
  }

  /**
   * Set the line style of the series
   * 
   * @param seriesLineStyle
   */
  public Series setLineStyle(SeriesLineStyle seriesLineStyle) {

    stroke = SeriesLineStyle.getBasicStroke(seriesLineStyle);
    return this;
  }

  /**
   * Set the line style of the series
   * 
   * @param basicStroke
   */
  public Series setLineStyle(BasicStroke basicStroke) {

    stroke = basicStroke;
    return this;
  }

  /**
   * Set the line color of the series
   * 
   * @param seriesColor
   */
  public Series setLineColor(SeriesColor seriesColor) {

    strokeColor = seriesColor.getColor();
    return this;
  }

  /**
   * Set the line color of the series
   * 
   * @param color
   */
  public Series setLineColor(java.awt.Color color) {

    strokeColor = color;
    return this;
  }

  /**
   * Sets the marker for the series
   * 
   * @param seriesMarker
   */
  public Series setMarker(SeriesMarker seriesMarker) {

    this.marker = seriesMarker.getMarker();
    return this;
  }

  /**
   * Sets the marker color for the series
   * 
   * @param seriesColor
   */
  public Series setMarkerColor(SeriesColor seriesColor) {

    this.markerColor = seriesColor.getColor();
    return this;
  }

  /**
   * Sets the marker color for the series
   * 
   * @param color
   */
  public Series setMarkerColor(java.awt.Color color) {

    this.markerColor = color;
    return this;
  }

  public Collection<?> getXData() {

    return xData;
  }

  public Collection<Number> getYData() {

    return yData;
  }

  public Collection<Number> getErrorBars() {

    return errorBars;
  }

  public BigDecimal getxMin() {

    return xMin;
  }

  public BigDecimal getxMax() {

    return xMax;
  }

  public BigDecimal getyMin() {

    return yMin;
  }

  public BigDecimal getyMax() {

    return yMax;
  }

  public BasicStroke getStroke() {

    return stroke;
  }

  public Marker getMarker() {

    return marker;
  }

  public Color getStrokeColor() {

    return strokeColor;
  }

  public Color getMarkerColor() {

    return markerColor;
  }

  public String getName() {

    return name;
  }

  void replaceXData(List<Number> newXData) {

    xData = newXData;
    calculateMinMax();
  }

  void replaceYData(List<Number> newYData) {

    yData = newYData;
    calculateMinMax();
  }

  private void calculateMinMax() {

    // xData
    BigDecimal[] xMinMax = findMinMax(xData, xAxisType);
    xMin = xMinMax[0];
    xMax = xMinMax[1];

    // yData
    BigDecimal[] yMinMax = null;
    if (errorBars == null) {
      yMinMax = findMinMax(yData, yAxisType);
    }
    else {
      yMinMax = findMinMaxWithErrorBars(yData, errorBars);
    }
    yMin = yMinMax[0];
    yMax = yMinMax[1];
    // System.out.println(yMin);
    // System.out.println(yMax);
  }
}
