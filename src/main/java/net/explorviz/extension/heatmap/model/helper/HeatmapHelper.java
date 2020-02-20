package net.explorviz.extension.heatmap.model.helper;

import java.util.ArrayList;
import java.util.List;
import net.explorviz.landscape.model.application.Application;
import net.explorviz.landscape.model.landscape.Landscape;
import net.explorviz.landscape.model.landscape.Node;
import net.explorviz.landscape.model.landscape.NodeGroup;
import net.explorviz.landscape.model.landscape.System;

public class HeatmapHelper {

  public HeatmapHelper() {}

  /**
   * List all applications contained in a landscape.
   *
   * @param landscape
   * @return
   */
  public static List<Application> findLandscapeApplications(final Landscape landscape) {
    final List<Application> applications = new ArrayList<>();

    for (final System system : landscape.getSystems()) {
      for (final NodeGroup nodegroup : system.getNodeGroups()) {
        for (final Node node : nodegroup.getNodes()) {
          applications.addAll(node.getApplications());
        }
      }
    }
    return applications;
  }
}
