#  python对接es的相关操作

```python
# python 对接 elasticserch 的相关操作


from elasticsearch import Elasticsearch
es = Elasticsearch('192.168.235.21:9200')

# 连接es
#print(es.ping())

# 创建一篇文档
#print(es.index(index='p1', doc_type='doc', id=1, body={"name":"张三"}))
# {'_index': 'p1', '_type': 'doc', '_id': '1', '_version': 1, 'result': 'created', '_shards': {'total': 2, 'successful': 1, 'failed': 0}, 'created': True}
# index: 如果索引存在就更新索引，索引不存在则创建索引

# 根据id查询文档
#print(es.get(index='p1', doc_type='doc', id=1))
# {'_index': 'p1', '_type': 'doc', '_id': '1', '_version': 1, 'found': True, '_source': {'name': '张三'}}

# 根据id删除文档
#print(es.delete(index='p1', doc_type='doc', id=1))
# {'found': True, '_index': 'p1', '_type': 'doc', '_id': '1', '_version': 2, 'result': 'deleted', '_shards': {'total': 2, 'successful': 1, 'failed': 0}}

# 创建测试数据
# print(es.index(index='p1', doc_type='doc', id=1, body={"name":"张三"}))
# print(es.index(index='p1', doc_type='doc', id=2, body={"name":"李四"}))
# print(es.index(index='p1', doc_type='doc', id=3, body={"name":"王五"}))

# 查询
# body = {
#     "query":{
#         "match":{
#             "name":"张三"
#         }
#     }
# }
#print(es.search(index='p1', body=body))
# {'took': 51, 'timed_out': False, '_shards': {'total': 5, 'successful': 5, 'skipped': 0, 'failed': 0}, 'hits': {'total': 1, 'max_score': 0.51623213, 'hits': [{'_index': 'p1', '_type': 'doc', '_id': '1', '_score': 0.51623213, '_source': {'name': '张三'}}]}}
# 结果过滤
#print(es.search(index='p1', body=body, filter_path=['hits.hits._source']))
#{'hits': {'hits': [{'_source': {'name': '张三'}}]}}
# 查看搜索返回的结果数量
#print(es.search(index='p1', body=body, filter_path=['hits.hits._source', 'hits.total']))
#{'hits': {'total': 1, 'hits': [{'_source': {'name': '张三'}}]}}

# 查询 _source
# print(es.get_source(index='p1', doc_type='doc', id='1'))
# {'name': '张三'}

# 返回满足查询条件的记录数
# print(es.count(index='p1', doc_type='doc', body={
#     "query":{
#         "match":{
#             "name":"张三"
#         }
#     }
# }))
# {'count': 1, '_shards': {'total': 5, 'successful': 5, 'skipped': 0, 'failed': 0}}

# es.create 也是创建索引，但是只能创建一次，如果创建的索引已经存在会报错
# print(es.create(index='p2', doc_type='doc', id=1, body={"name":"王五"}))
# print(es.get(index='p2', doc_type='doc', id=1))
# 执行两次，报错的信息如下
# elasticsearch.exceptions.ConflictError: ConflictError(409, 'version_conflict_engine_exception', '[doc][1]: version conflict, document already exists (current version [1])')

# 删除满足查询条件的记录
# body={
#     "query":{
#         "match":{
#             "name":"王五"
#         }
#     }
# }
# print(es.delete_by_query(index='p2', doc_type='doc', body=body))
# {'took': 143, 'timed_out': False, 'total': 1, 'deleted': 1, 'batches': 1, 'version_conflicts': 0, 'noops': 0, 'retries': {'bulk': 0, 'search': 0}, 'throttled_millis': 0, 'requests_per_second': -1.0, 'throttled_until_millis': 0, 'failures': []}

# 查询数据是否存在
# print(es.exists(index='p1', doc_type='doc', id=2))

# 打印 es的相关信息
# print(es.info)
# <bound method Elasticsearch.info of <Elasticsearch([{'host': '192.168.235.21', 'port': 9200}])>>

# 查询索引库是否存在
#print(es.indices.exists(index='p1'))

# indices 方法创建索引
body={
    "mapping":{
        "properties":{
            "name":{
                "type":"text"
            },
            "age":{
                "type":"long"
            }
        }
    }
}
print(es.indices.create(index='p3', body=body))

# indices 方法删除索引
#print(es.indices.delete(index='p3'))
```

