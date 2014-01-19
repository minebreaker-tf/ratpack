/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ratpack.handling.internal;

import ratpack.handling.RequestOutcome;
import ratpack.http.Request;
import ratpack.http.SentResponse;

public class DefaultRequestOutcome implements RequestOutcome {

  private final Request request;
  private final SentResponse response;
  private final long closedAt;

  public DefaultRequestOutcome(Request request, SentResponse response, long closedAt) {
    this.request = request;
    this.response = response;
    this.closedAt = closedAt;
  }

  @Override
  public Request getRequest() {
    return request;
  }

  @Override
  public SentResponse getResponse() {
    return response;
  }

  @Override
  public long getClosedAt() {
    return closedAt;
  }
}
