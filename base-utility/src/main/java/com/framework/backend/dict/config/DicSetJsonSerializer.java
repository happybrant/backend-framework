package com.framework.backend.dict.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.framework.backend.dict.entity.Dict;
import com.framework.backend.dict.service.DictService;
import com.framework.backend.utils.SpringBeanUtils;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author fucong
 * @description 字典集序列化
 * @since 2025/9/25 16:27
 */
public class DicSetJsonSerializer extends JsonSerializer<Object> implements ContextualSerializer {

  private String dicSetName;
  private String fieldName;
  private String format;

  public DicSetJsonSerializer(String fieldName, String dicSetName, String format) {
    this.dicSetName = dicSetName;
    this.fieldName = fieldName;
    this.format = format;
  }

  public DicSetJsonSerializer() {}

  /**
   * 设置数据项名称的字段名
   *
   * @return
   */
  private String getItemNameField() {
    return "dicName";
  }

  /**
   * 设置数据项值的字段名
   *
   * @return
   */
  private String getItemValueField() {
    return "dicCode";
  }

  /**
   * 根据注解参数中的数据集名称查询数据集
   *
   * @param dicSetName
   * @return
   */
  private List<Dict> getDicSet(String dicSetName) {
    DictService dictService = SpringBeanUtils.getBean(DictService.class);
    return dictService.listByGroupCode(dicSetName);
  }

  /**
   * 查询数据集中是否有匹配的数据项
   *
   * @param itemValue 待匹配的数据项
   * @param dictList 完整的数据集
   * @return 匹配结果
   */
  private String getItemName(List<Dict> dictList, String itemValue) {
    AtomicReference<String> itemName = new AtomicReference<>("");
    dictList.stream()
        .filter(r -> r.getCode().equals(itemValue))
        .findFirst()
        .ifPresent(item -> itemName.set(item.getName()));
    return itemName.get();
  }

  /**
   * @param itemValue
   * @param jsonGenerator
   * @param serializerProvider
   * @throws IOException
   */
  @Override
  public void serialize(
      Object itemValue, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    String itemValueString = itemValue.toString();

    // 字段值为空值时就直接返回null值
    // 将空值统一转换成null值是为了便于使用Map来接收转换后的DicSet字段值
    if (StringUtils.isBlank(itemValueString)) {
      jsonGenerator.writeNull();
      return;
    }

    // 注解中的数据集名不为空时就查询指定的数据项
    if (StringUtils.isNotBlank(this.dicSetName)) {
      // 根据注解值查询数据集
      List<Dict> dicSet = getDicSet(dicSetName);
      // 数据集存在时
      if (null != dicSet && !dicSet.isEmpty()) {
        // 根据值查询对应的数据项名称
        String itemName = getItemName(dicSet, itemValueString);
        // 当能匹配到数据项时就拼接返回前端的数据结构
        if (StringUtils.isNotBlank(itemName)) {
          String activeFormat = format;
          // 如果注解上未设置format属性就默认采用flat拉平的方式
          if (StringUtils.isBlank(activeFormat)) {
            activeFormat = "flat";
          }

          if ("wrap".equalsIgnoreCase(activeFormat)) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField(this.fieldName + "_" + getItemNameField(), itemName);
            jsonGenerator.writeObjectField(this.fieldName + "_" + getItemValueField(), itemValue);
            jsonGenerator.writeEndObject();
          } else if ("flat".equalsIgnoreCase(activeFormat)) {
            // 平铺显示字典name和value信息，形如：
            // "status": "active",
            // "status_dicName": "启用",
            jsonGenerator.writeString(itemValueString);
            // 新增字典name字段
            jsonGenerator.writeFieldName(this.fieldName + "_" + getItemNameField());
            jsonGenerator.writeString(itemName);
          }
        }
        // 不能查询到值对应的数据项时就直接返回值
        else {
          jsonGenerator.writeString(itemValueString);
        }
      }
      // 数据项不存在时就直接返回值
      else {
        jsonGenerator.writeString(itemValueString);
      }
    }
    // 数据集名称为空时就直接返回值
    else {
      jsonGenerator.writeString(itemValueString);
    }
  }

  @SneakyThrows
  @Override
  public JsonSerializer<?> createContextual(
      SerializerProvider serializerProvider, BeanProperty beanProperty) {
    // 字段的json属性不为空时
    if (beanProperty != null) {
      // 使用AnnotationUtils获取json字段上的字段注解信息
      Class<?> jsonClass = beanProperty.getMember().getDeclaringClass();
      Field field = jsonClass.getDeclaredField(beanProperty.getName());
      DicSet dicSet = AnnotationUtils.findAnnotation(field, DicSet.class);
      if (dicSet == null) {
        // 获取json类上的字段注解信息
        dicSet = AnnotationUtils.findAnnotation(jsonClass, DicSet.class);
      }
      // 如果能得到注解，就将注解的name值传入MyJsonSerializer
      if (dicSet != null) {
        // 获取元注解上的name参数值，此值已经被子注解上的name值覆盖
        String dicSetName =
            Objects.requireNonNull(
                    AnnotatedElementUtils.getMergedAnnotationAttributes(field, DicSet.class))
                .getString("name");
        String format =
            Objects.requireNonNull(
                    AnnotatedElementUtils.getMergedAnnotationAttributes(field, DicSet.class))
                .getString("format");
        // 创建json序列化类的实例
        return new DicSetJsonSerializer(beanProperty.getName(), dicSetName, format);
      }
      return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
    return serializerProvider.findNullValueSerializer(beanProperty);
  }
}
