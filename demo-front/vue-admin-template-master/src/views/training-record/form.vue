<template>
  <div class="staff-container">
    <Row class="staff-searchBar">
      <Col span="5"></Col>
      <Col span="14">
        <div v-if="this.$route.params.id">
          <div class="staff-title">修改培训记录</div>
        </div>
        <div v-else>
          <div class="staff-title">添加培训记录</div>
        </div>
        <Form
          ref="formValidate"
          :model="formValidate"
          :label-width="80"
          :rules="ruleValidate"
          @on-validate="saveState"
        >
          <FormItem label="培训老师" prop="trainingTeacher">
            <Select
              @on-change="saveState"
              v-model="formValidate.trainingTeacher"
              placeholder="请选择培训老师"
            >
              <Option
                v-for="teacher in teachers"
                :key="teacher"
                :value="teacher"
                >{{ teacher }}</Option
              >
            </Select>
          </FormItem>

          <FormItem label="培训日期" prop="trainingDate">
            <DatePicker
              @on-change="saveStateS"
              v-model="formValidate.trainingDate"
              placeholder="请选择培训日期"
            ></DatePicker>
          </FormItem>

          <FormItem label="培训内容" prop="trainingContent">
            <Input
              @on-change="saveState"
              v-model="formValidate.trainingContent"
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 8 }"
              placeholder="请输入培训内容"
            ></Input>
          </FormItem>

          <FormItem label="参与人员">
            <div>
              <Table
                border
                size="small"
                ref="selection"
                :columns="selectTableColumns"
                :data="selectTableData"
                @on-selection-change="addOrRemoveItems"
                @on-select-all="addOrRemoveItems"
              ></Table>

              <Page
                size="small"
                :total="this.total"
                :current="this.selectTableSearchParam.current"
                :page-size="this.selectTableSearchParam.size"
                @on-change="changePage"
              >
              </Page>
            </div>
            <Table
              border
              size="small"
              :columns="displayTableColumns"
              :data="formValidate.list"
            ></Table>
          </FormItem>

          <FormItem>
            <Button type="primary" @click="handleSubmit('formValidate')"
              >提交</Button
            >
            <Button
              @click="handleReset('formValidate')"
              style="margin-left: 8px"
              >重置</Button
            >
          </FormItem>
        </Form>
      </Col>
      <Col span="5"></Col>
    </Row>
  </div>
</template>
  
  <script>
import staffApi from "@/api/staff";
import trainingRecordApi from "@/api/training-record";

import Sortable from "sortablejs";

export default {
  name: "TrainingRecordForm",
  //第一种
  computed: {
    formValidate() {
      return this.$store.state.trainingRecordForm.formValidate;
    },
  },
  data() {
    return {
      total: 0,
      teachers: [],
      selectTableData: [],
      ruleValidate: {
        trainingTeacher: [
          {
            required: true,
            message: "请选择培训老师",
            trigger: "blur",
          },
        ],
        trainingDate: [
          {
            required: true,
            message: "请选择培训日期",
            trigger: "change",
            pattern: /.+/,
          },
        ],
        trainingContent: [
          {
            required: true,
            message: "请填写培训内容",
            trigger: "blur",
          },
        ],
      },
      selectTableColumns: [
        {
          type: "selection",
          width: 55,
          align: "center",
        },
        {
          title: "姓名",
          key: "name",
        },
        {
          title: "性别",
          key: "sex",
          minWidth: 30,
          render: (h, params) => {
            const row = params.row;
            const text = row.sex === 1 ? "男" : row.sex === 2 ? "女" : "未设置";
            return h("Tag", {}, text);
          },
        },
        {
          title: "身份证号码",
          key: "idcardNumber",
          minWidth: 80,
        },
        {
          title: "部门",
          key: "department",
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
      ],
      displayTableColumns: [
        {
          type: "index",
          width: 55,
          align: "center",
        },
        {
          title: "姓名",
          key: "name",
        },
        {
          title: "性别",
          key: "sex",
          minWidth: 30,
          render: (h, params) => {
            const row = params.row;
            const text = row.sex === 1 ? "男" : row.sex === 2 ? "女" : "未设置";
            return h("Tag", {}, text);
          },
        },
        {
          title: "身份证号码",
          key: "idcardNumber",
          minWidth: 80,
        },
        {
          title: "部门",
          key: "department",
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
      ],
      selectTableSearchParam: {
        keyword: "", // 查询关键字
        current: 1, // 页码
        size: 5, // 每页记录数
        field: "name", // 查询条件
        sortBy: "gmt_modified", // 排序条件
        sortAsc: false, // 正序排序
      },
    };
  },
  created() {
    let id = this.$store.state.trainingRecordForm.formValidate.id;
    if (this.$route.params.id) {
      // 修改培训记录
      if (this.$route.params.id != id) {
        // 不是之前正在进行的修改，重新获取数据
        this.query();
      }
    } else {
      // 新增培训记录
      if (id != "0") {
        // 之前进行过未完成的修改，需要清除
        this.$store.commit("trainingRecordForm/RESET_FORMVALIDATE");
      }
    }

    this.queryAllName();
    this.getSelectTableData();
  },
  methods: {
    handleSubmit(formValidate) {
      this.$refs[formValidate].validate((valid) => {
        if (valid) {
          if (this.$route.params.id) {
            this.edit(this.formValidate);
          } else {
            this.add(this.formValidate);
          }
        } else {
          this.$Message.error("请按照格式填写数据");
        }
      });
    },
    handleReset(formValidate) {
      this.$refs[formValidate].resetFields();
    },
    changePage(page) {
      this.selectTableSearchParam.current = page;
      this.getSelectTableData();
    },

    addOrRemoveItems(selection) {
      let idArray = selection.map((item) => {
        return item.id;
      });

      let removeArray = this.selectTableData.filter((item) => {
        if (!idArray.includes(item.id)) {
          return item;
        }
      });

      let removeArrayId = removeArray.map((item) => {
        return item.id;
      });

      this.formValidate.list = this.formValidate.list.filter((item) => {
        if (!removeArrayId.includes(item.id)) {
          return item;
        }
      });
      this.formValidate.list = this.formValidate.list.filter((item) => {
        if (!idArray.includes(item.id)) {
          return item;
        }
      });
      this.formValidate.list.push.apply(this.formValidate.list, selection);
      this.saveState();
    },

    query() {
      let formdata = { id: this.$route.params.id };
      trainingRecordApi.query(formdata).then((response) => {
        // debugger 设置断点调试
        if (response.success === true) {
          this.$store.dispatch(
            "trainingRecordForm/SET_FORMVALIDATE",
            response.data.data
          );
        }
      });
    },
    queryAllName() {
      staffApi.queryAllName().then((response) => {
        // debugger 设置断点调试
        if (response.success === true) {
          this.teachers = response.data.data;
        }
      });
    },
    getSelectTableData() {
      staffApi
        .staffComplexQuery(this.selectTableSearchParam)
        .then((response) => {
          // debugger 设置断点调试
          if (response.success === true) {
            this.selectTableData = response.data.items;
            this.total = response.data.total;

            // 修改 selectTable 的选中状态
            this.selectStatus();
          }
        });
    },
    add(formData) {
      trainingRecordApi.add(formData).then((response) => {
        // debugger 设置断点调试
        if (response.success === true) {
          this.$Message.success("新增成功");
          this.$store.dispatch("trainingRecordForm/RESET_FORMVALIDATE");
          this.$router.push("/training-record/list");
        }
      });
    },
    edit(formData) {
      trainingRecordApi.edit(formData).then((response) => {
        // debugger 设置断点调试
        if (response.success === true) {
          this.$Message.success("修改成功");
          this.$store.dispatch("trainingRecordForm/RESET_FORMVALIDATE");
          this.$router.push("/training-record/list");
        }
      });
    },
    saveState() {
      this.$store.dispatch(
        "trainingRecordForm/SET_FORMVALIDATE",
        this.formValidate
      );
    },
    saveStateS() {
      this.formValidate.trainingDate = new Date(
        this.formValidate.trainingDate.getTime() + 8 * 60 * 60 * 1000
      );
      this.saveState();
    },
    selectStatus() {
      let idArray = this.formValidate.list.map((item) => {
        return item.id;
      });

      // 判断当前数据是否有选中的数据,已被选择的设置_checked为true
      this.selectTableData.forEach((item) => {
        if (idArray.includes(item.id)) {
          item._checked = true;
        }
      });
    },
  },
};
</script>
  
  <style lang="scss" scoped>
.staff {
  &-container {
    margin: 30px;
  }
  &-title {
    font-size: 30px;
    line-height: 46px;
    text-align: center;
    margin-bottom: 30px;
  }
}
</style>