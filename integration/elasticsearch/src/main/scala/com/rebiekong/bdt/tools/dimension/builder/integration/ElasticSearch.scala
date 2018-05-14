package com.rebiekong.bdt.tools.dimension.builder.integration

import com.rebiekong.bdt.tools.dimension.builder.core.dimension.{DiscreteDimension, RangeDimension}
import com.rebiekong.bdt.tools.dimension.builder.request.models.RequestItemModel
import com.rebiekong.bdt.tools.dimension.builder.request.where._
import org.elasticsearch.action.search.SearchType
import org.elasticsearch.client.Client
import org.elasticsearch.index.query.{QueryBuilder, QueryBuilders}
import org.elasticsearch.search.sort.{SortBuilders, SortOrder}

import scala.collection.convert.wrapAll._
import scala.language.implicitConversions

/**
  实验中
 */
class ElasticSearch(requestModel: RequestItemModel) {

  def read(client: Client): Unit = {
    val d = requestModel.field.table.split(".")
    val search = client
      .prepareSearch(d(0))
      .setTypes(d(1))
      .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
    search.setPostFilter(caseWhereToEs(requestModel.where))
    search.setSize(requestModel.pagination.pageSize)
    requestModel.pagination.orderBy
      .foreach(
        o => search.addSort(SortBuilders.fieldSort(o.fieldName).order(
          if (o.isAsc()) {
            SortOrder.ASC
          } else {
            SortOrder.DESC
          }
        ))
      )
  }

  def caseWhereToEs(where: Where): QueryBuilder = {
    where match {
      case andWhere: AndWhere =>
        boolQuery("and", caseWhereToEs(andWhere.left), caseWhereToEs(andWhere.right))
      case orWhere: OrWhere =>
        boolQuery("or", caseWhereToEs(orWhere.left), caseWhereToEs(orWhere.right))
      case notWhere: NotWhere =>
        boolQuery("not", caseWhereToEs(notWhere.input))
      case valueWhere: ValueWhere =>
        valueWhere.dimension match {
          case rangeDimension: RangeDimension[_] =>
            QueryBuilders.rangeQuery(rangeDimension.name)
              .from(rangeDimension.lowerBound)
              .to(rangeDimension.upperBound)
          case discreteDimension: DiscreteDimension[_] =>
            QueryBuilders.matchPhraseQuery(discreteDimension.name, discreteDimension.value)
        }
      case _ => null
    }
  }

  def boolQuery(boolType: String, queryBuilder: QueryBuilder*): QueryBuilder = {
    val rs = QueryBuilders.boolQuery()
    boolType match {
      case "and" =>
        queryBuilder.filter(s => s == null).foreach(s => rs.must(s))
      case "or" =>
        queryBuilder.filter(s => s == null).foreach(s => rs.should(s))
      case "not" =>
        queryBuilder.filter(s => s == null).foreach(s => rs.mustNot(s))
    }
    rs
  }


}

object ElasticSearch {
  implicit def elasticsearchConvent(requestModel: RequestItemModel): ElasticSearch = {
    new ElasticSearch(requestModel)
  }
}
