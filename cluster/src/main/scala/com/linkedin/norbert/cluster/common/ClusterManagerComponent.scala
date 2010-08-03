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
package common

import actors.Actor
import util.GuardChain

trait ClusterManagerComponent {
  val clusterManager: Actor
  val notificationCenter: Actor

  sealed trait ClusterManagerMessage
  object ClusterManagerMessages {
    case object Connect extends ClusterManagerMessage
    case class AddNode(node: Node) extends ClusterManagerMessage
    case class RemoveNode(nodeId: Int) extends ClusterManagerMessage
    case class MarkNodeAvailable(nodeId: Int) extends ClusterManagerMessage
    case class MarkNodeUnavailable(nodeId: Int) extends ClusterManagerMessage
    case object GetNodes extends ClusterManagerMessage
    case class Nodes(nodes: Set[Node]) extends ClusterManagerMessage
    case object Shutdown extends ClusterManagerMessage
    case class ClusterManagerResponse(exception: Option[ClusterException]) extends ClusterManagerMessage
  }

  trait BaseClusterManager {
    this: Actor =>

    protected var connected = false
    protected var currentNodes = Map.empty[Int, Node]

    protected val ifConnected = GuardChain(connected, reply(ClusterManagerMessages.ClusterManagerResponse(Some(new NotYetConnectedException))))
    protected def nodes = Set.empty ++ currentNodes.values
  }
}