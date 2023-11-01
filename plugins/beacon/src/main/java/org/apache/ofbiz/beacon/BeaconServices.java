/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.apache.ofbiz.beacon;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.websocket.Session;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

public class BeaconServices {
    private static final String MODULE = BeaconServices.class.getName();

    public static Map<String, Object> sendBeaconPushNotifications(DispatchContext dctx, Map<String, ? extends Object> context) {
        String beaconId = (String) context.get("beaconId");
        String message = (String) context.get("message");
        Set<Session> clients = BeaconWebSockets.getClients();
        try {
            synchronized (clients) {
                for (Session client : clients) {
                    client.getBasicRemote().sendText(message + ": " + beaconId);
                }
            }
        } catch (IOException e) {
            Debug.logError(e.getMessage(), MODULE);
        }
        return ServiceUtil.returnSuccess();
    }
}
