<%--
  #%L
  CAS Mock
  %%
  Copyright (C) 2016 - 2018 Emory University
  %%
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  #L%
--%>
<html>
    <head>
        <title></title>
        <style type="text/css">
            body {
                font-family: Arial,Helvetica Neue,Helvetica,sans-serif; 
                padding: 1em;
            }

            dt {
                float: left;
                width: 5em;
                text-align: right;
                padding: .25em;
                clear: left;
            }

            dd {
                float: left;
                padding: .25em 0;
            }

            dl:after {content:"";display:table;clear:both;}
        </style>
    </head>
    <body>
        <h2>Mock CAS</h2>
        <div>
            Mock CAS does not validate users but will behave like a CAS server that does.
            It will return valid CAS responses although actual authentication has not been achieved.
        </div>
        <div>            
            Currently hard coded to reply with user "superuser" and nonsense CAS ticket values.
        </div>
        <%
            request.setAttribute("serviceUrl", "https://localhost:8443/cas-mock");
        %>   
        <h3>login</h3>  
        <p>
            <a href="login?service=${serviceUrl}">login?service=${serviceUrl}</a>
        </p>
        <fieldset>
            <legend>parameters:</legend>
            <dl>
                <dt>service</dt>
                <dd>required - required and used by cas-mock</dd>
            </dl>
        </fieldset>
        <h3>verify the ticket and be done</h3>  
        <p>
            <a href="serviceValidate?ticket=ST-xxxxxxxxxxxxxx&service=${serviceUrl}">serviceValidate?ticket=ST-xxxxxxxxxxxxxx&service=${serviceUrl}</a>
        </p>
        <fieldset>
            <legend>parameters:</legend>
            <dl>
                <dt>ticket</dt>
                <dd>required - ignored by cas-mock</dd>
                <dt>service</dt>
                <dd>required - ignored by cas-mock</dd>
                <dt>pgtUrl</dt>
                <dd>optional - used by cas-mock</dd>
            </dl>
        </fieldset>
        <h3>verify the ticket and enable further proxying</h3>  
        <p>
            <a href="serviceValidate?ticket=ST-xxxxxxxxxxxxxx&service=${serviceUrl}&pgtUrl=https://localhost:8443/cas-mock/pgtCallback">serviceValidate?ticket=ST-xxxxxxxxxxxxxx&service=${serviceUrl}&pgtUrl=https://localhost:8443/cas-mock/pgtCallback</a>
        </p>
        <div>
            The presence of the pgtUrl makes the CAS server call that URL with the request parameters; pgtId and pgtIou. 
            e.g. <i>https://localhost:8443/cas-mock/pgtCallback?pgtIou=PGTIOU-xxxxxxxxxxxxxxxx&pgtId=TGT-xxxxxxxxxxxxxxxx</i>
        </div>
        <fieldset>
            <legend>parameters:</legend>
            <dl>
                <dt>ticket</dt>
                <dd>required - ignored by cas-mock</dd>
                <dt>service</dt>
                <dd>required - ignored by cas-mock</dd>
                <dt>pgtUrl</dt>
                <dd>optional - used by cas-mock</dd>
            </dl>
        </fieldset>
        <h3>get a proxy ticket</h3>  
        <p>
            <a href="proxy?targetService=${serviceUrl}&pgt=PGTIOU-xxxxxxxxxxxxxxxx">proxy?targetService=${serviceUrl}&pgt=PGTIOU-xxxxxxxxxxxxxxxx</a>
        <p>
        <fieldset>
            <legend>parameters:</legend>
            <dl>
                <dt>pgt</dt>
                <dd>required - ignored by cas-mock</dd>
                <dt>targetService</dt>
                <dd>required - ignored by cas-mock</dd>
            </dl>
        </fieldset>
        <h3>verify the proxy ticket</h3>
        <p>
            <a href="proxyValidate?service=${serviceUrl}&ticket=PT-xxxxxxxxxxxxxxxxxxxxxxxxxxxx">proxyValidate?service=${serviceUrl}&ticket=PT-xxxxxxxxxxxxxxxxxxxxxxxxxxxx</a>
        </p>
        <fieldset>
            <legend>parameters:</legend>
            <dl>
                <dt>ticket</dt>
                <dd>required - ignored by cas-mock</dd>
                <dt>service</dt>
                <dd>required - ignored by cas-mock</dd>
                <dt>pgtUrl</dt>
                <dd>optional - used by cas-mock</dd>
            </dl>
        </fieldset>
        <h3>logout</h3>  
        <p>
            <a href="logout">logout</a>
        </p>
    </body>
</html>