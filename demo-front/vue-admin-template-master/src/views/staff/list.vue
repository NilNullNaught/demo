<template>
  <div class="staff-container">
    <Row class="staff-searchBar">
      <Col span="8"></Col>
      <Col span="8"></Col>
      <Col span="8">
        <!-- 搜索 -->
        <Input
          v-model="searchParam.keyword"
          clearable
          search
          @on-search="getTableData"
          @on-clear="clearKeyword"
        >
          <span slot="prepend">根据姓名查询</span>
        </Input>
      </Col>
    </Row>

    <!-- 表格主体 -->
    <Table
      border
      stripe
      v-bind:data="tableData"
      :columns="tableColumns"
      row-key="id"
      @on-column-width-resize="saveTableState"
    >
      <template slot-scope="{ row }" slot="action">
        <Button
          type="success"
          size="small"
          style="margin-right: 5px"
          @click="edit(row)"
          >修改</Button
        >
        <Button type="error" size="small" @click="remove(row)">删除</Button>
      </template>
    </Table>

    <!-- 分页 -->
    <div style="margin: 10px; overflow: hidden">
      <div style="float: right">
        <Page
          show-total
          show-elevator
          :total="this.total"
          :current="this.searchParam.current"
          :page-size="this.searchParam.size"
          @on-change="changePage"
        >
        </Page>
      </div>
    </div>
  </div>
</template>
  
 <script>
import { mapGetters } from "vuex";
import staffApi from "@/api/staff";
import Sortable from 'sortablejs'

export default {
  name: "StaffList",
  data() {
    return {
      total: 0, // 总记录数
      searchParam: {
        keyword: "", // 查询关键字
        current: 1, // 页码
        size: 10, // 每页记录数
        field: "name", // 查询条件
        sortBy: "gmt_modified", // 排序条件
        sortAsc: false, // 正序排序
      },
      tableData: [], // 表格数据
      tableColumns: [
        {
          title: "姓名",
          key: "name",
          sortable: true,
          resizable: true,
          width: 180,
        },
        {
          title: "性别",
          key: "sex",
          resizable: true,
          width: 180,
          render: (h, params) => {
            const row = params.row;
            const text = row.sex === 1 ? "男" : row.sex === 2 ? "女" : "未设置";
            return h("Tag", {}, text);
          },
        },
        {
          title: "身份证号码",
          key: "idcardNumber",
          resizable: true,
          width: 180,
        },
        {
          title: "部门",
          key: "department",
          sortable: true,
          resizable: true,
          width: 180,
          render: (h, params) => {
            const row = params.row;
            const text =
              row.department === 1
                ? "业务部"
                : row.department === 2
                ? "采购部"
                : row.stadepartmenttus === 3
                ? "行政部"
                : "未设置";
            return h("Tag", {}, text);
          },
        },
        {
          title: "学历",
          sortable: true,
          resizable: true,
          width: 180,
          key: "formalSchooling",
          render: (h, params) => {
            const row = params.row;
            const text =
              row.formalSchooling === 1
                ? "初中"
                : row.formalSchooling === 2
                ? "高中"
                : row.formalSchooling === 3
                ? "大专"
                : "本科";
            return h("Tag", {}, text);
          },
        },
        {
          title: "邮箱",
          key: "email",
          resizable: true,
          width: 180,
        },
        {
          title: "操作",
          slot: "action",
          resizable: true,
          width: 150,
          align: "center",
        },
      ],
    };
  },
  created() {
    // 当页面加载时获取数据
    this.getTableData();
  },
  mounted() {
    let width = this.$store.state.staffList.tableColumnWidth;
    if (width.length !== 0) {
      let i = 0;
      this.tableColumns.forEach((element) => {
        element.width = width[i];
        i++;
      });
    }
  },
  methods: {
    getTableData(page = 1) {
      staffApi.staffComplexQuery(this.searchParam).then((response) => {
        // debugger 设置断点调试
        if (response.success === true) {
          this.tableData = response.data.items;
          this.total = response.data.total;
        }
      });
    },
    changePage(page) {
      this.searchParam.current = page;
      this.getTableData();
    },
    clearKeyword() {
      this.searchParam.keyword = "";
      this.getTableData();
    },
    search(keyword) {
      this.searchParam.keyword = keyword;
      this.getTableData();
    },
    edit(row) {
      this.$router.push({ name: "StaffEdit", params: { id: row.id } });
    },
    remove(row) {
      let dataform = { id: row.id };
      staffApi.delete(dataform).then((response) => {
        if (response.success === true) {
          this.getTableData();
        }
      });
    },
    saveTableState() {
      let columnWidthArray = this.tableColumns.map((obj) => {
        return obj.width;
      });
      this.$store.commit("staffList/SET_TABLECOLUMNWIDTH", columnWidthArray);
    },
  },
};
</script>
  
  <style lang="scss" scoped>
.staff {
  &-container {
    margin: 10px;
  }
  &-searchBar {
    margin-bottom: 5px;
  }
}
</style>
  