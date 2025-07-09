package cn.xuyinyin.prism

import com.coralogix.zio.k8s.client.K8sFailure.syntax._
import com.coralogix.zio.k8s.client.model.K8sNamespace
import com.coralogix.zio.k8s.client.v1.configmaps
import com.coralogix.zio.k8s.client.v1.configmaps.ConfigMaps
import com.coralogix.zio.k8s.model.core.v1.ConfigMap
import com.coralogix.zio.k8s.model.pkg.apis.meta.v1.ObjectMeta

object ZioK8sSpec {

  def main(args: Array[String]): Unit = {
    val test = for {
      _ <- configmaps.create(
        ConfigMap(
          metadata = ObjectMeta(name = "test"),
          data = Map("x" -> "y"),
        ),
        K8sNamespace.default)
      containsTest <-
        configmaps
          .get("test", K8sNamespace.default)
          .ifFound
          .map(_.nonEmpty)
    } yield containsTest
    test.provideLayer(ConfigMaps.test)
  }
}
