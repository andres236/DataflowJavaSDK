/*
 * Copyright (C) 2015 Google Inc.
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
package com.google.cloud.dataflow.sdk.runners.inprocess;

import com.google.cloud.dataflow.sdk.runners.inprocess.InProcessPipelineRunner.UncommittedBundle;
import com.google.cloud.dataflow.sdk.transforms.AppliedPTransform;
import com.google.cloud.dataflow.sdk.util.WindowedValue;

class PassthroughTransformEvaluator<InputT> implements TransformEvaluator<InputT> {
  public static <InputT> PassthroughTransformEvaluator<InputT> create(
      AppliedPTransform<?, ?, ?> transform, UncommittedBundle<InputT> output) {
    return new PassthroughTransformEvaluator<>(transform, output);
  }

  private final AppliedPTransform<?, ?, ?> transform;
  private final UncommittedBundle<InputT> output;

  private PassthroughTransformEvaluator(
      AppliedPTransform<?, ?, ?> transform, UncommittedBundle<InputT> output) {
    this.transform = transform;
    this.output = output;
  }

  @Override
  public void processElement(WindowedValue<InputT> element) throws Exception {
    output.add(element);
  }

  @Override
  public InProcessTransformResult finishBundle() throws Exception {
    return StepTransformResult.withoutHold(transform).addOutput(output).build();
  }

}
