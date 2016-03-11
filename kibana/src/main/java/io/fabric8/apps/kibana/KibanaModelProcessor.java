/*
 * Copyright 2005-2015 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package io.fabric8.apps.kibana;

import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodSpecBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.fabric8.kubernetes.generator.annotation.KubernetesModelProcessor;

import java.util.List;

@KubernetesModelProcessor
public class KibanaModelProcessor {

    public void on(PodTemplateSpecBuilder builder) {
        List<EnvVar> currentEnvVars = builder.getSpec().getContainers().iterator().next().getEnv();
        String imageVersion = System.getProperty("docker.image.version", System.getProperty("project.version"));
        if (imageVersion == null) {
            throw new AssertionError("No `docker.image.version` or `project.version` system properties defined so can't default the docker image version!");
        }
        PodSpec podSpec = new PodSpecBuilder(builder.getSpec())
                .addNewContainer()
                .withName("kibana-config")
                .withImage("fabric8/kibana-config:" + imageVersion)
                .withEnv(currentEnvVars)
                .endContainer()
                .build();

        builder.withSpec(podSpec);
    }
}
