/*
  Copyright 2017 Kakao Corp.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.kakao.kakaotalk.request;

import androidx.annotation.NonNull;

import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.message.template.MessageTemplateProtocol;
import com.kakao.message.template.TemplateParams;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author kevin.kang. Created on 2017. 5. 16..
 */

abstract class DefaultTemplateRequest extends AuthorizedApiRequest {
    private JSONObject jsonObject; // This can't be final

    public DefaultTemplateRequest(final @NonNull TemplateParams templateParams) {
        this.jsonObject = templateParams.toJSONObject();
    }

    @Override
    public String getMethod() {
        return POST;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put(MessageTemplateProtocol.TEMPLATE_OBJECT, jsonObject.toString());
        return params;
    }
}
