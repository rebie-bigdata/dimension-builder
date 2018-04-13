# dimension-builder

针对以下的场景
0. A=2,B=3，0<P<5
1. 根据C分组
2. 从表a,b中取维度
    * a.D
    * a.D when E=1
    * a.D when E=2
    * b.D
3. 结果表结构为

|C             | a.D        | a.D when E=1  | a.D when E=2 | b.D |
| ------------- |:-------------:| -----:|:-----:|:-----:|
|C1|10|2|3|7|
|C2|8|4|2|6|

4. 要求a,b满足:字段C，D必须存在
5. 传输状态的json
```JSON
{
  "group_by": [
    "C"
  ],
  "order_by": [],
  "limit": null,
  "where": {
    "type": "and",
    "left": {
      "type": "value",
      "sub_type": "discrete",
      "field": "A",
      "value": 2
    },
    "right": {
      "type": "and",
      "left": {
        "type": "value",
        "sub_type": "discrete",
        "field": "B",
        "value": 3
      },
      "right": {
        "type": "value",
        "sub_type": "range",
        "field": "P",
        "lower_bound": "0",
        "upper_bound": "5"
      }
    }
  },
  "fields": [
    {
      "table": "a",
      "field": "D",
      "name": "D"
    },
    {
      "table": "a",
      "field": "D",
      "name": "D1",
      "where": {
        "type": "value",
        "sub_type": "discrete",
        "field": "E",
        "value": 1
      }
    },
    {
      "table": "a",
      "field": "D",
      "name": "D2",
      "where": {
        "type": "value",
        "sub_type": "discrete",
        "field": "E",
        "value": 2
      }
    },
    {
      "table": "b",
      "field": "D",
      "name": "bD"
    }
  ]
}
```
