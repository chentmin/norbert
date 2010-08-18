/*
 * Copyright 2009-2010 LinkedIn, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.linkedin.norbert
package cluster
package zookeeper

import common.{ClusterManagerMessages, ClusterManagerSpecification}
import org.specs.util.WaitFor

class ZooKeeperClusterManagerSpec extends ClusterManagerSpecification with WaitFor {
  val clusterManager = new ZooKeeperClusterManager(delegate, "test", "localhost:2181", 30000)

  "A connected ZooKeeperClusterManager with mock ZooKeeper" should {
    doBefore {
      import ClusterManagerMessages.RemoveNode
      setup
      waitFor(250.ms)
      (0 until 10).foreach { i => clusterManager !? RemoveNode(i) }
      nodesChangedEventCount = 0
    }

    doAfter { cleanup }

    "behave like a ClusterManager" in { clusterManagerExamples }
  }
}