# hwc-ocr-android
This project shows how to use Huawei Cloud OCR service with Thailand ID card

Modified from https://developer.huaweicloud.com/intl/en-us/sdk?OCR

Follow https://support.huaweicloud.com/intl/en-us/sdkreference-ocr/ocr_04_0028.html

# Prerequisites Libraries
- Huawei apigateway java-sdk-core (https://repo.huaweicloud.com/repository/maven/huaweicloudsdk/com/huawei/apigateway/java-sdk-core/3.0.12/)
- Alibaba fast json (https://github.com/alibaba/fastjson)
- Joda time (https://github.com/JodaOrg/joda-time/releases)
- OkHttp (https://search.maven.org/artifact/com.squareup.okhttp3/okhttp)
- Okio (https://search.maven.org/artifact/com.squareup.okio/okio)

# Configuration
Provide Huawei credentials at /app/src/main/res/values/strings.xml
- ak: Access Key
- sk: Secret Key
- region: Huawei region ID
