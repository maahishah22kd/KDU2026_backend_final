import { Body, Controller, Delete, Get, Param, ParseIntPipe,Post, HttpCode} from '@nestjs/common';
import { TodosService } from './todos.service';
import type { Todo } from './entities/todo.entity';
import { CreateTodoDto } from './dto/create-todo.dto';


@Controller('todos')
export class TodosController {
    constructor(private readonly todoService: TodosService){}

    @Get()
    findAll(): Todo[]{
        return this.todoService.findAll();
    }

    @Get(':id')
    findById(@Param('id', ParseIntPipe) id:number): Todo{
        return this.todoService.findById(id);
    }

    @Post()
    create(@Body() createTodoDto: CreateTodoDto): Todo {
    return this.todoService.create(createTodoDto);
    }

    @Delete(':id')
    @HttpCode(204)
    deleteById(@Param('id', ParseIntPipe) id:number){
        return this.todoService.deleteById(id);
    }
}
