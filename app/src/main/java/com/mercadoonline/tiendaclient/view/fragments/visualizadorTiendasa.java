package com.mercadoonline.tiendaclient.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mercadoonline.tiendaclient.R;
//import com.mercadoonline.tiendaclient.adapter.multitienda_adapter;
import com.mercadoonline.tiendaclient.adapter.VistaMultitienda;
import com.mercadoonline.tiendaclient.models.recibido.Puesto;
import com.mercadoonline.tiendaclient.models.recibido.ResponseTiendas;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */


public class visualizadorTiendasa extends Fragment {

    List<ResponseTiendas> ls_tienda= new ArrayList<>();
    ResponseTiendas puestin =new ResponseTiendas();

    FloatingActionButton agregar;
    View vista;

    RecyclerView recyclerView;

    public visualizadorTiendasa() {
        // Required empty public constructor
    }
    void UI(){
        agregar= vista.findViewById(R.id.agregar_tienda);
        recyclerView=vista.findViewById(R.id.recycler_view_multitienda);


        puestin.setNombre("carlin");


       // puestin.setImagen("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxITEhUSEhIWFRUXFRcaFRgXFRcWFxUVFRcXFxUXFxYYHSggGB0lGxUVITEhJSkrLi4uFx8zODMtNygtLisBCgoKDg0OGxAQGi0iICUtLS0tLS0tKy0rLS0tLS0tLS0tLS0rLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tK//AABEIAKYBLwMBIgACEQEDEQH/xAAbAAACAgMBAAAAAAAAAAAAAAAEBQMGAAIHAf/EAEYQAAIABAQDBgMFBQQIBwAAAAECAAMEEQUSITFBUWEGEyJxgZEyobFCUsHR8CNicoKiBxSS4RUzQ1NjssLSFiSDhJPT8f/EABoBAAIDAQEAAAAAAAAAAAAAAAMEAQIFAAb/xAAoEQACAgEEAQQDAQADAAAAAAAAAQIDEQQSITFBBRMiMhRRgWFxobH/2gAMAwEAAhEDEQA/AOdSDBDCBZJgtY6IQEmiI1MTzxA4h2o5jrBe7JyuN+PKCqmm7t7A35EGEcloaYejTGCjUxp0sUuTwOsNpjM8N/SGvdy5HxNduCr4m8z90dTCufKeTpcBiLn91eZ/D/KK1X4gWuq3C/NjxJMTbZ4QGnTOzmfRdj2z7o+ASxb7xLn1C2+sZN/tUqeDJ6Sf+5jHOC0aM0IzSl2OwqhWsRRccR7ZtPzO4XPcElZSqXGo8RU20vvvtrFdr8aZiSCbnctb2tt67wvlTNSL2BBB1IHrbqBEMtC7BRuTAsJBMksmXmuzGyj4j+A5mNJtUbjLdQp8IB26359Y9q5o+BfgXbqeLQPEHFoocbSdLMueATbUHaYP3bfDM+RhRWZZV1S+Zt7m5Vb+Fb87fraNaWX3YMxhqNFB5/r6GAHYkknUk6xCOyeiJpEpnNlH5DzMZSUxc24frQQ+pKf7MsXPPcDnYfaPX/8AInJwPIw5EsX8R4Dn5LufWDWuBqoC8Rxtx209IZCnlSBnnNcnYblvz+kbLXrUI8vIUIUsl7a25cvnFck4K3IoULNZSQDtewHMX46wb/oUTVvKUgjhr7HkYa0FRTSiKc6nZnIGVm4i99BeCQGp5oIPhO/VfzEdk4qE+YQuW2o0hY8dA7Q4dTnNNNwRqwH277MPXeKHOGukSmQQQXiMtVZVUWIRc1nzhmIuWBG2404QTh0kTFCm4ytcEAHQjkdxmA94XObknmeAt8uEQQaqt4KlqBt6wMghnS4c7C58I5t+USkcCImsWPBcIOYFlu32V3A6t16Rvh1AARkBJ+8Rc/yj9esX3AMDN1ly7d61ySdRLX7THn+JsOZgu9RQSKS5Yz7K4KC2ZxdJZBc/7yaLFU6quhPWw5iGuM4qM1r+cG1rJTyxKliyqDbW5N9SSeJJJJPUxRsTnMSTE0w3vczI1upzLaiDEa8F/F8N9ByEJMdKggKdCL7841rJhhXNN4ZmsINpY8ZB520K6kaw1mQsqRrCFjNFMhlwcm0Ay4NSE5FWBIYMlmA7QRJaOgcZPEC2g2aIGKw5UQF4bRtMYKsXDBsLNODNmC54DmToBCHAJmRgw0MWObVNMIF7KOZ0LHc6XO2nvzh6OWsIWlJbsS4QnxGRNnMQTubux4ngAOQAHsIglYLLG93I8/oIutFhcq13ffY5L/Mtb5Q4p6eVLGVXU34mWv8A0kQKbfSC/l0dJnMp9DKU2Mq38jflArUMg6Wt6kfL/KOmYh2fmm7GSJqnXNLN29VNm/w5oQzcJltfKdjZlYXynkynxKehhdyDLEuikTsEF/Cx8tG+Wh+UCf3MyS5J1CG2hBBJA2PnFwq8FIFwpy80IZf8DQgxWTZWGbN4V+8LeP7rbekcuSGitZId0NAJYDuMzn4V5ef5x7hdFYd4Rc7IOZ5w5WSssF5jWOxNrm4+xLB4jiTovne0S4OSbKvibEqhP2rk+en4394ClSizBRx+XMwwr5f7NDyJHuAfzgjBqM2zW1bRfLmPX6Ry6ICqKj+yNAPiO3mCeHU+QhnOq1kjIi5phGxG3IsPonvHiXACy99wd1X/AIh5nfKPNuIiJqEIyMLnxeInUktfxH1hey5RePI3p9O5/J9Ak6ROzZ2uzaeLQnTbThHnfz7g+O4BA8Otjvwh6Fja0KfkyND2YfoRrLnMuQoMvVVHz3hjQT2dGkPrMQXl8yBuvXT6jlBT2AJJsOJMCJbvpExeL2vzBB/C8EqvcpYYG+iGxtEUyqLS+7Oo1AP7rDMPYiKi5vFnUceH4WNvrCGlpr+JtFHHmeQh9GUH4bJIUdTc+6EQpZbknrFupqW2cPuFNrbAsFyj0tb0hGcPI30A49OkWisnGmDUpL3H2RyvqdB+MW+gwMuA7HThfX2UaQhwueiA3U3J4W24cYfSMXU5VY2QC2Szrm6lkYN7ERd1yfRZFowbCgT+zAts81zllrzu53P7q687bxe8K7iRLIlv3jH4nVWfMQOaAgDkOHzip4P2gkgKf7lKbKLKczFlHJe8ViPeLTI7Sypg1R06EBh/QSflC8oTzyilmcCjEw73JVhfiQAPYm/yit1ikDURcqmoV72cG244jzEVrGigB11h2mXgwb4LcU+vOsKZkGVc3UwE5i10jU0te2JDM2hbUwym7QtqYz5scIZYg1ICliDlhZnAkxY9lxPMSIQIvBHEpiMrG4j20NwRVsmpZpEM6eqIhXKEEiHISwL2VqXY5GMMFy30gvD6xnYDNaK5BlHMym8HTTErKYxWcHRcP7TzJYC2DWhnU4nST1zVEvu2A0mDwstuTgbdDp0ij4bUF2uTt9IOq6ebU+GxSUuupC5+rcQOQsYXnTBsnSe65d8C/Esbld4ZclmdeE0Jkbyy/a87DfaE9XR5zlY7rfax0YE36w9l00lfD3gTnlFx76Ex5MopQZTLfNe6/CQSTa1rk32intpdGqrYPjILhuFM5uksu1iJaLoTlGpvsoGmvlxsCDWUTMwlzEAbxWKgiwIuFtfQL3badSeMdB7O0k+lnd7NQd2y5JjIQVlroUOrXsDuco+Ik7QB2vwufTzmqQpMsTVmqwsbNmDZGG4BNxfaxGsIWt7sIYonHP8A4cxm0Js8o73B+e4/lIglmWWABysOijQn8PeLf/aJhMqU8msk/wCpqFvYDQXUMtvMG9v3Y27IdiFrZEyZM8LEkAkaqy7IvIAWJPNospfFZBTabckNsCwKT/oybUIyzXmS2OYW8Fhcjo2mvLaKW8wBgh4gkehEDTqSuwtps6SSZKu0ubuZTEMZdpi6A7aEa68IUzsVE0yyoyut9OF7jY8RCllDzlDulvXKZZbQNX1qyhc6k3sPLrwEEg6A8xAz0qs4dtbAWHAcb+cKrGeR9vgFpqd5x7ydov2E4Hqw/CJsRbLkI0N2t0OUqvzZY1r8XlytCczfdG/qeEKZFVVVMwdzLudQoAvYt1Ol9IZqrlKSljCQlfbGMXFPLYbUFUXxbWsBz4ac4EppfelQBuQoA2FyB+MMqzsrPki9Sx717ZU3JvrdidtAxt0guhw5pChls3jBJYGykqRw3sLnhtDrnFcZFaYPDlg9myyzHfxTDoN2ubIvz84Ixvs5NTu0ICvlzOugEpDogY3IzGzG3DS/S59icHVA1bP+FAcmYW1HxMBw5ADjffSIZ0tJs4vNmKHmNm1DZEGgUX3NlAGmW9t4vXNt8dIFJJPBUcO7NBuJbjp4V9WO46iLPhfZMsAUQW+8AFX/AORwS38qmHM7DjKOYZZgGoJAyg81UaDz1PWMHac2NxtudYO5Ta+Is9TFPGAqXgMhBZ3J52H4tf5ARKXo5Y8KKSOLXY+7Xio4t2lBPhN4QzsaJ4xEapP7MFO2TXBZ8bx82OXaKjV4wW4wPU4hcamErTNYZ4iuBSmne8zGLPeI2EaSjEphWx5NSMcIHmwuqBDKdAM1YVkWIJK6wXaI5KROwgLJNSsRMsExo6wWCIZABGwEYRHqw3BA2TSxBAWIZMEiDIgjtGwaPWiMxZMq4J9kkutZDdTaCJfaCYt/Fe41veFU3pA5Ug6i0EXIP2lEaSqlmMXXsvJOh001HTS34xTKG2kWimrgiDJ8XEmLSXGDOun8i/ycTUeHfmPzglAAndqomySCGkta4B4SydLfuNoOBAFooVBUO7b6xa8FSYWGbYQldRFLJbTaiecINoqKnnyWpmlnLTTby1dSpQMCZdg3BczKOBycYrOKYjNw2YRSllSZlLrMksy94qKhZSLfEqrs24MXqe2SbKmfZb9lM6BzeU3o/h/9WFXb6Rejmn7hRvQMM39JMZdmUzeoabwyiDttOLNZadg7ZnQhspbS7BGJyk2BOtr62uSSnxqhop0tnSVPSqOoYd0JJJOxUNcDU62J+ka1NMjizqD+uBhfLwsq10mMF4i+vlfaBq9ryPvSQAb1Ur4v2gy2W1za3lxjRpNU+hbu5bMMx1uqk2JsPFYDWw1h9OmhRcm2oA8ybAQ27Ny6VpwWrDZDYLZigDH7xBBtFY3c5wi860otZYupcBwqUFInTKqYD41eROlymHEKxUW89YsmE9ppEnugtIVWQCJKKy2uwILTJjEuxsTpbjc30taKzs9hklc7ykVebzHN/K7anyhLhdTQTqlJFPRSshzFndBeyqT4RvvbU+0FdzYlGqLWcMEw7Dp+I1DT5gCJci4NwoJuVW4Fzoo2sMsWk01Kk4SQmcSApWWozOZ7DMZj30GVAtmYgeM8bQ6JlU8pnCBVRdFVQLnZVUDiSQAOsL0VpMux1cktMPOY5zOfK5IHIACCVR3PItqL9qwuiHFcNn1BGeZLCg3WUM9geBZx8Z9LDgOMLa/s+bHOt+qsG/BT84J/0t4tTaJKrG1UXJ0tp1h6EJw6EFrExLTu0lcuY5futqPRhosIccqlN7aE8uMSY5juYmwEVWpqDDsY+WLyu918IGq5pBOsBtUx5UzCYFMVY9VH48m0yeY0lNrEMwxtJOsUkGSSHMgxMYFpzBQMAkWIZ0BPBk+AJjQvJEk8qNZzRrJePZwgMiSaPSI8ianlhtL25dYYgjmCusaCGRw6YSFym526wHOkspIIsRwhyESmDeSIMRYHpheDVS0HUQMp4eCMy4ieVBqi8aukdsBO5oGopAzXPDWIcaqBMYWG2l+cEPLMDtTmCRr5yc7+AemmERYaSmcsAQRcAi/I6gwto6HMwEdLwjAUyyyJZTwC5JvnP3hyiLbFBAHXGx5AsNwoixEWqgzC2kFU2GgCwtf5wZIw8iM2y5SDRpUehd2mnMKOZa4JMsAjcEzEFx5b+kSU1QldR628aNLmgfZexVvzHQiJMSqZfeyJAcM7T1zKDcqFlzJniA2+AbwDPoHopzTpCl5EyxnylF3QjQTZQ+1YbruQNL2AhKzkdq4X+nLEBF1b4kJR/wCJDlb5iPCYs3bnC1EwVkmzSZ1s5XYTNlf+YaHkVHExVSYQsWJG5VNTimhNjFR+2lqdFVlbzNxc+QH1MXPHJ9HMkyDImB5yy0WbkBKGyDdtswItpfrFbqZRe6lVtfS9ydtxYi28eyJLJYArlHAKR88xizktqRXY9+QyZOZrZ2Jyiy3N7AbAX2EWz+zKjzTpk3giBR5ufyX5xTCYt07HBh9OtHIINZN8UwixFOWAN33GYLay9Lnhea1lldQ8R2ryPe0eNs1XIppJukmbKepI2zZ1Al/y3uepXlFyr5PhMU3CcB/u+HTZj6zHAdifi0ZX1PMnU+fSLzVC4tDsX1gx71HpHJu0VSUcjaEM/EWIsTe0XHthgua7iKJUUhU2MbVUlKJ5+yrbN5CcOpRNJuRwsCbXgLFJChiF1EFUtMdxePZ9KeUXxySrlHhIr06VaBGWGVchEQ0lPmMdtH67sQ3MVTZZjWVvD6fRiF06mtFXANXqIzJqcwYpgCRBaGASiMo1qDC6adYYT4WzYVnEsbyDBZEByd4O4QCSJRreJqdTe4iICD8OexuQD5w5VArZLEQuTUTC41JbgeXlEGMUbqxZ9yd+cOw8tQGQDMeB4QTV0wnJmYkael+FobisCfv48lQpEhvIpyeES0eFnNa0dEwLssAoLbxay2Na5B2SdsviUabgjBM4B2uYESnJjq9XgFxYQrbsyANoDDVROnW2ik0eGK18178OXlBh7PlxdRrxi10uCqp8RA53sB84JerpJXxT5Y6ZgT7DeOlqHn4nV1Saw0VfDeyzhgbRbKuol0knvJl9NFF9Wbgo/Wkc5xLGpjzHcOygsbAMwAX7IsDytAM2sZl8TFiDpmLMbHffQCDvSTnhzfA5XptvIfPxCpqKgMpczWPgEu4KjkttgOfqY6XgeHVfdAVlQzHfIllNvuvNXVvS3UmOf9iu0Emk76bMBaYwVUUDgLknMdALlfbaCsQ7V1lRsf7vLOwS+cj+I6+1oR9Qt52RSSXkchRKfS4OgzJEtZtMiKqBZjvlUAf7GYpNuOr79YPnG5MUD+zySTUvN1OWU13YliWJWwLHc2vF7JjInLgiVeyeCnduZbyJbT5SB5TXFRIOiTCx0mAgXSZewzDiRcG2nMJNdLmHwMeYDaNb00PmI6zjmLItWlNNt3UyUQ99gXYhD/Tb+aOP472eaTVTZBNipzS77MpO4I23B9YphS7HNPmP9DHlZtMxXqDY+8aSpAS9iet2J+u0KDUz5Wjaj94X9mB+sD1FW8zRjp90aA+nGIjTL98DTl/gwm4yVYGSdRqH3seBXqN77RaP7LsGNTPefM8SIRmLal3bxEEne+hP+cUw0DBczb3AVRqzMxsBHXsDmChmUdCLf6smo6zpuoJ/mFvIiCvbGOIi92f6XPHRmo54/wCG/wBNIknTp6gZkE0cWl+Ejl+zcm48mJ6RHiQLU09RuZbAeZBAiiUva+rlGxCzQDYq/hcW0tnH4iLxlwjPjQ7M4LJMIqHKC4y6spBVxyujC487RXa/CJSzGM11UcLmx9BvDqm7Y0VRZKhTKcbd4LZSdLrNX4fPSFmNdhW1m0kwzAdcjsCxH7sw/F5H3jR01yziT2oRs0Kz8z3CsPkTbiU+YjcWINueu4jet7PGx0ioyKmbTzeKTEOoIsQeII4i3vF/pe3FMZamYrh7eJVW4B6EkaQ3dCyDTh8kwM/T4eDnOMYM4v4dBANHItwjrtfhSVMpZkv4XAYeR5iK7/4cKgkjaOr1Ca57FbKXBbSlz5EK6qVF4q8LJUkDaKpVJraDxakLVScWI1XWJ1MNV7tCAFzZhZwRtflEeK0SIfC1/wDOByXJvwktuRXPML5sHzlgGaIVsiXTT6Mkwap0gGVBqbQnJEhSy7wTTSjHlIl4sGC0GdgI1oxSWRCdz+oTheDs4zEaQ9ocBLm3ARaKCjlogVmUMRsSBBUuqkSt2v0UFj/SDCk9S+Uin46b5A6Ds4q2NoLxLHJFKMrEs9vgUXPrwHrCvHe0T5bS7oD/AI29vgHz8opE+bc3jq6JW82dDNVcY/UsFd24qG+BUljyzt7nT5RW6zHKh/inzPIMVHsukRT0MKp8yNGqqmHUUMrCPZ8++pNz11+sDPOiGZNgSbOg7sS6Lb14CZk+If7zaBXmxCXPCKuzINzLFgWQ3uPFuoO1ufmDFmwXC3qpmUaKLF25DkOp4Rz+VOICsp1Go/EHodRHZeztcVpZTyZcgS3UMGmVJViTvnHdaG+hHC1uEYHqNEo2bvDG6NXmvb5RZaGlSUglyxZQLD8STxMSxWp3afL8U2gH/vD/APVA83terIypOos5UhbVgNmI0Nmlj6xnbWRhlP7UVpm1U1xquYqv8KeH8CfWJMRmGvw6VWqLz6Y93PH3lXQn1DK3TMeUKsQoamVLZhIdxbRpdpy+eaUWt62hv/YzQzSaiYSO4Yd2yHXPMFiTbhZSQeea3CIjB4eRyyUVFOL6AqHs+9VI7ynKuR8SEgMCdVIvoykeXEQVg3Yqod/HK7hftMct7cQoG5+UZQzGwrEGlnWS23WWxufUb+jCOgdtsZSRRsyEAzBlUj94G7X5BQT7RVRWCJ3TykvJQcFw2VPxIZLf3ak/asT9orcSieZLBn8lhVWYmaia9SCRncsp4hQbS/ZQsOq8ChwZybrPrSBxBCuNBb7OWSp/mJ5xT8GmgywOK6H6iLSjiBan5Sb/AId0wLERU04mDdls45OPiHvAPaTs8J4MyXZZo9A45HryMU7sLjyyJplTXCy5mxY2s4GnuNPaL8uNSz8KT3HMU80D0ZlAPnHRy0LWL2p8HPZsjwlXXXYg7g8ukCUlVU0hzU8w5Rup1X1TY+Y1i19qZSzxnlyZqzRzCKHHI3fQ8jFLxXvpUos8tlv4QSUIzNoPhY/oQxVv3KKQdWwlDMgDEcYaonPOe2Zzc2vYWAAtfoBGqT4UKdco1294f0FEFF5o3+EfUx6pNQikZqmnyizdnO3L00sSWliYgJt4irC+tr6g6xZP/GlHOFiWlkjZ10/xLce9o5I0yxPIRtLnwCejqk93TIlCMlydrw6jR0uCrKdipBHyiodouzFnJUWuYqNDXPLbNLdkbmpI97b+sXfBe2JmWl1QB1FpgFrdXG1uo25cYBLT21PdHlCtmjW3gpFdhjI1mGsRzqBgt2Fo65N7OI8zvTYrYEW1B5a8RFU7ZUv3dAIiGoU2khTZOK56OdzKYtoIBq6VkNiLGLdRTFlg5lueHnCWvXMbmJnDIam9cISIIMlRAyWMTS4z7I4H08lnwPD8+kWekw2ZJYNYwtwCYDaZcKeI5mOjYTNDLqAYdtscTMde59kFPiJKHMugGvP06wNmNtd/1YfheGeN2ARRYXYs3UINP6mU+kI51UvA3/hu30vC0MPlEyU/r2I8SWYWLMp9NdPSE82bYw+n4ml7eK/LK35QtqamU5ymxPIggj3EORn4C+7OC+UeBNUVF79YVznhrX0B3TXpx9DxiKmwzOrG+wvtv5RZz29hYWKa4EkwnXpvAU59DD+XkuyhdLBTzJ1P4GFNZRlWt9ngekS23HJCszLaAM0GrT5ZYbifpECy8xQW338hB8w8IJVHciltm1pIWyja68tRHkuXLDEzELDha2nO8a1RyuDwP6MbzFiZ0xthsmWhdKqe+PkZyjS2FsoPJtD/AFQT3CH7KkeQiukagkXHEXIv6jUQ2oK2UBlC9304E/xfnGDrdBKnmGWja0utVnEsJhtPTrLbPLBlt95CUPupEO5OOTMwacqziNmN0mgchOl5X9yYUiNljMUmOyrjLtDvFjTVhDPPqJLgAKXVZ6DKSRqMr7k7k7mNJGBpnRjW0sxFZSys7yc6qb5dVawNgPK8RSZQKAc7+8C1cgKIJl9gvZXSbRcO1E+RWSTKnTaNbao6znmvLa1syhUW/lexioUuG4fT38c+qfY5SKeUfa7/ADgCPQIh2Po6GnUfLGK43PS4pRJpFO/dSw80/wAU2ZcsethEAmNMBE+bNm34s5J1ga0T0svMwF7RHuSCe1BeAQ4HJsTby6/KFM6SgfwDRdL8zx9tveLLi1QJSAW8R0X8T+ukVsLbyjZ9LolKXuS6Rmeo6hRj7ce2CNPs5I4GGUzFHZV1Ph3P0+kI5Cl204k/oxYZFIMmTmNT15xsJbuTNnYq8I9xKqvLlnvUcvdnULZpbDSzHjcQFKnxPUKxqFluZbd2oW8tQFIAuL6anXcwwWUv3V9hFqs7S0tQosEkzoY0s+xB/Vo9SQn3R7ROtEp20gyYSN8ZF37I4/3ZEiYf2bG0s/7sngf3ST6HpsV2ipCWinSU0AOulvOHEqvntlUTGY6BRZCT0uRc+ZMZ9unSnvj/AEDdDfHgR4pQMpvaE02XHQ6jD7SyZ5u1r3tb0ih1xF4mMtyMmacJYQhqlsY0QxLVmIAYSvXJtad5gh5h80raxi/9ncaOgMUmXTDN4NRwiwYTTMGBtD9sYyjyZblKMjoNdkdZZZAxvfXWyqMx06sEHrCbEpllJ5CGanw+Uv8A5nH/AGQjxtv2b+V/YiMOX2wbukXwyVxZbkl7aagWbKxsddbaC/ltEb1AUWyFB5XHndb+5hzTEIqgi/hF/wAfnCzEQAxANxfSL/lyrfCQWzRRv7bAmmgi4Nx01gadNYXZGs1vQ+f5wPPJQ5l2+0o/5gOf184xpl+MamnuhqIcGJqNPPS2Y/7F9MxCuTuGBPpqfleDaoB5ZHHh5xpMsQRz3gOgnEpYnUaHzGn4QyoJfEE5tveDYYlwx5Gw876xK43/AF+t4mRQL8NST6xBIN1B53PoTcfK0ErjtSRM57nuA6+RmU8xqIhpmzKDDGaQBc7QCJWViy6od7fZPMDiIs+HkvB5jg1ZIjZYKsCLg3HONGSCYTJTwZSVry9PiXkTt5H8Is2HVNA4HeVbyTyemY/1S2YRVSseZYzrvS6LHuxh/wCDtevugsJnVMKpsOOn+kZbdNEP9RixvhmHIR3kyUxVb/tJqCw3vkuB7iOFyzaG/aOuSc6kO00hVBdxZjYcukKS9JSeFIv+dN8suXa3t1RWEqmp5U8iy94yfspYPFQBmmW38Nh1iDC5uFzUu+eUqizT5jrJWa/ESpJYk68ALDbeOd5I9WUBsILL0mtpJMpHWTT7LXjFbhyG1PMqJx55FVB/M4BPoDCA18w7EJ5C59z+UDhIkVINV6XRDlrP/JWevuksZJp00u2ZiSbW1N4gqr5CBudB66RuTbTc8BxP65xvKkm921PAcB5desOqKitsUJubbyz2hpQgsOWp5w/wV1uQRvudNAPOFKiN2mEIcvxHQeZ0HzMdKPxwC3NyyyafSjvDMDZi2YnT7xuNukeMbW87e8PKKjld2M24GhHThAuL047pXVbeMe2aBxmksF+WwaTDiklBRmbjoB8zCilIuLw4pcRSYGsBZQQPewMWsb6QepdmKdYcdn3nBz3KK7EbspORf4rgKD7m3G0K8PpmmzUlL8TsFHQbsfRQT6RfqGWsmkQIACR4jbVmBsWbmYU1dqS2dthnLEdzEHaSe2WzkX45QQL9ASY5zXzNTFt7QVJYmKTiERBbYmVBqdgvntrEV4ya8aAwrbyzZqWEdI7KBbbA/OLxh+GAxQuz1OwYW1G+kdPwgaCCap7XwZ1L38Myop7HKPtSmt5oysB7M0VXFReW4/db6Raa0FmacL2kHwW42/1+nHwkqOqwDiuDtMOeSQVYX3tvxHSMuT5ybOnko8MSypHgYsCRlvprYDcxXHkvMbJLUsx2AFz/AJRfMP7NaftXJuLWUkC3ImH+H4dLlCyIFHQfMniYBJbhl6lQ65KPgvYFm8VS1h9xT9W/L3hB297NCjmLMlC1PNNgB/s5trlP4WALDqGHKOvzqhU+IgQoxppFRJeTMsEdSLkhSDwZb7EGxB5gQfT2+xPcv6I6hy1C+Rw0vAFLpOdfvaj6/wDUfaGFRTmXNeSzKzI2UlTcNsVYeYINuF7cIMlSEV78Strnhv8AmI9FKxOKkjMhDlxYlxV8ssgbnQfr9bxOqWAHIW9okaYgqAji4B+mpP0j2rcFiRtfSLwlukyJx2xSPaaUCwB24+UC9pcJ7t88rRSL2GlonkTipuIbTapZknxnxrt1iZp5yRXLaUL+8MDe+vPn584kFc3FQfW35w6q8MVlJUAE79fyivzpRU2YWIiuWh2OyYSKwfdMZ/e16wHHhi6mS6Yh6VK34+0F4pXK01iWY7DxLlbQAagaCFVOhLCwvr1tbraJ5dESbsbdBrFk22UcIR7ZsascATE0sOdwFHuYklSQuw/OJbRfH7BSkvCPESNr8B6ngPzMegX6D5/5RMiR3YJs1lyrfieJiYCPQsYI4G2egR4urDkv1O3sPrHvCPUFvPj5xD5I6GNJWZbgnSBsVxFxKYA2FxpEMBYyf2R8xA5RWGwlb+SCa6ryJpuRYe0MezEi6k8NPkP84qbzi7C/C3y2H4xcMEcovMbG3z+d/lA92eRzGyOPLOj/ANn1AuZ51tvAp6mzPb0yj3h/RSA0gX5v/wA7Rv2doe5kS5fEC7fxt4n/AKiY8wt//LoRxDH/ABMT+MYNlrsm5f6FlFKOGULtZh+QlgNIoGILHUe1bkqwAvfcRzGuG8alTzDkxsKNvAhZdY9tBJlxG6wKcDXjYde7NSAq2G/OLgGySmcbhTbz4X9YyMgGqfyFqF0H00ju0VAb2GpO7HdmPUm59YFpxkYyvs2LJ+6L6qegJ06G3DXIyM40EGS+fKK7ifae0ppwBWUrFDYK0wspINgfCouN9fIRkZFHxEvCKlLkolb26m3Jky1S/wBp7zZh9ToPK1opdfL7x2mkKHY6kIoUnqqgAeYtGRkBhZKMk0zU9itxw0EYM4L2ItYkG3A8xG3aMGWii/2gfnGRkeprk5Vpv9HmnFRtaX7ElE5M0k8F+sMS8ZGQ3V0Dv+xgaJ30APOMjIIwJpNqDaBZuu+sZGRKRaIO1On3RGLJUbKIyMicIvuf7JF6R7GRkccbRsFjIyOIfRIsNqOmVkud7xkZFJ9FUbVFOFWF7RkZEQIkZHt48jIuUNhC/HP9V/MIyMgdn1YSr7oHwlBnGbXUe5IF/n8o6L2YohMrJUoWCJ+1YfeEorlX/EyHyBjIyEr241Sa/Q6+bEdTqppWW7DcIxHmAbQuopXch5AN1UIydBMBzD/Gjn+YRkZGHSHu6K3jLEZjzjnmKS7GMjI2qfqYLfyFuSBqiMjImzoeobcj/9k=");
        ls_tienda.add(puestin);
        ls_tienda.add(puestin);
        ls_tienda.add(puestin);
        ls_tienda.add(puestin);
        ls_tienda.add(puestin);
        ls_tienda.add(puestin);
        //agregar=vista.findViewById(R.id.agregar_tienda);
       iniciar_recycler();
    }
    void Click(){
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroLocal local = new registroLocal();
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Contenedor_Fragments, local).addToBackStack("frag_regisLocal");
                fragmentTransaction.commit();

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         vista= inflater.inflate(R.layout.fragment_visualizador_tiendasa, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UI();
        Click();
    }




    private void iniciar_listado(){
        ls_tienda.clear();
        //ls_tienda=Global.mistiendas;
    }

    private void iniciar_recycler(){
        VistaMultitienda  adapter=new VistaMultitienda(ls_tienda);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

}
